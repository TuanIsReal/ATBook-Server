package com.tuanisreal;

import com.tuanisreal.config.Config;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@Slf4j
@AllArgsConstructor
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class AtbookUmsServiceApplication implements CommandLineRunner {

    private final RequestHandlerManager requestHandlerManager;
    private final MessageQueueManager messageQueueManager;
    private final Config config;

    public static void main(String[] args) {
        SpringApplication.run(AtbookUmsServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Ums service starting ...");
        long start = System.currentTimeMillis();
        messageQueueManager.initQueues();
        requestHandlerManager.startHandlers();
        long end = System.currentTimeMillis();
        log.info("Ums service running. Start time: " + (end - start));
    }
}
