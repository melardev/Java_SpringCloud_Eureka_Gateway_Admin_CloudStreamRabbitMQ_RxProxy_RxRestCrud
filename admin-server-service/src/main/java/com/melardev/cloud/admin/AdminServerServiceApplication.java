package com.melardev.cloud.admin;

import com.melardev.cloud.admin.models.Todo;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableHystrixDashboard
@EnableAdminServer
@EnableBinding(Sink.class)
public class AdminServerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerServiceApplication.class, args);
    }

    @StreamListener(target = Sink.INPUT)
    public void onTodoCreated(Todo todo) {
        System.out.println("Todo created " + todo);
    }
}
