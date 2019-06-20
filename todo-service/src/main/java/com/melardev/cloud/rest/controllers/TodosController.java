package com.melardev.cloud.rest.controllers;


import com.melardev.cloud.rest.dtos.responses.ErrorResponse;
import com.melardev.cloud.rest.entities.Todo;
import com.melardev.cloud.rest.messaging.TodoDataStreamSource;
import com.melardev.cloud.rest.repositories.TodosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.validation.Valid;
import java.util.function.Function;

@CrossOrigin
@RestController
@RequestMapping("/")
public class TodosController {

    @Autowired
    @Qualifier("messagingScheduler")
    Scheduler messagingScheduler;

    @Autowired
    TodosRepository todosRepository;

    @Autowired
    TodoDataStreamSource todoDataStreamSource;

    @GetMapping
    public Flux<Todo> getAll() {
        return todosRepository.findAllHqlSummary();
    }

    @GetMapping("/pending")
    public Flux<Todo> getPending() {
        return todosRepository.findByCompletedFalse();
    }


    @GetMapping("/completed")
    public Flux<Todo> getCompleted() {
        return todosRepository.findByCompletedIsTrueHql();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> getById(@PathVariable("id") String id) {
        return this.todosRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity(new ErrorResponse("Todo not found"), HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public Mono<Todo> create(@Valid @RequestBody Todo todo) {
        Mono<Todo> createdTodo = todosRepository.save(todo);
        createdTodo.subscribeOn(messagingScheduler)
                .map(new Function<Todo, Object>() {
                    @Override
                    public Object apply(Todo todo) {
                        todoDataStreamSource.getMessageChannel().send(MessageBuilder.withPayload(todo).build());
                        return todo;
                    }
                })
                .subscribe();
        return createdTodo;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> update(@PathVariable("id") String id, @RequestBody Todo todoInput) {

        Mono<ResponseEntity<Object>> res = todosRepository.findById(id)
                .flatMap(t -> {
                    String title = todoInput.getTitle();
                    if (title != null)
                        t.setTitle(title);

                    String description = todoInput.getDescription();
                    if (description != null)
                        t.setDescription(description);

                    t.setCompleted(todoInput.isCompleted());
                    return todosRepository.save(t)
                            .map(te -> te);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity(new ErrorResponse("Not found"), HttpStatus.NOT_FOUND));
        return res;
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id) {
        return todosRepository.findById(id)
                .flatMap(t -> todosRepository.delete(t)
                        .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(new ErrorResponse("Todo not found"), HttpStatus.NOT_FOUND));
    }


    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteAll() {
        return todosRepository.deleteAll().then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

}