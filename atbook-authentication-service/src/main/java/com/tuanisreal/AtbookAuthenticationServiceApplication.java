package com.tuanisreal;

import com.tuanisreal.cache.SessionManager;
import com.tuanisreal.config.Config;
import com.tuanisreal.repository.base.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@Slf4j
@AllArgsConstructor
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class AtbookAuthenticationServiceApplication implements CommandLineRunner {

    private final SessionRepository sessionRepository;
    private final RequestHandlerManager requestHandlerManager;
    private final MessageQueueManager messageQueueManager;
    private final Config config;

    public static void main(String[] args) {
        SpringApplication.run(AtbookAuthenticationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Authentication service starting ...");
        long start = System.currentTimeMillis();
        SessionManager.init(config, sessionRepository);
        messageQueueManager.initQueues();
        requestHandlerManager.startHandlers();
        long end = System.currentTimeMillis();
        log.info("Authentication service running. Start time: " + (end - start));
    }
}
