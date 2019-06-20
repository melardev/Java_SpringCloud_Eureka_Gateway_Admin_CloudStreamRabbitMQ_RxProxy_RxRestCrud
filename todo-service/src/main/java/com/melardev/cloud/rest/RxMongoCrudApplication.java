package com.melardev.cloud.rest;

import com.melardev.cloud.rest.messaging.TodoDataStreamSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(TodoDataStreamSource.class) // Bind this application to Cloud Data as the Source
public class RxMongoCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(RxMongoCrudApplication.class, args);
    }

}
