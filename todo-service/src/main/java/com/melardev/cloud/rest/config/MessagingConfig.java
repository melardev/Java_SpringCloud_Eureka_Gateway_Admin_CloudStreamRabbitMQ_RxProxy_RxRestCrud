package com.melardev.cloud.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
public class MessagingConfig {
    @Bean("messagingScheduler")
    Scheduler messagingScheduler() {
        return Schedulers.fromExecutor(Executors.newCachedThreadPool());
    }
}
