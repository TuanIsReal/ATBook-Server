package com.tuanisreal;

import com.tuanisreal.rbmq.MessageQueueManager;
import com.tuanisreal.server.GatewayServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@Slf4j
@AllArgsConstructor
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class AtbookGatewayMainApplication implements CommandLineRunner {

    private final GatewayServer gatewayServer;
    private final MessageQueueManager messageQueueManager;

    public static void main(String[] args) {
        SpringApplication.run(AtbookGatewayMainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("API Gateway starting ...");
        long start = System.currentTimeMillis();
        messageQueueManager.createQueuesAndStart();
        gatewayServer.start();
        long end = System.currentTimeMillis();
        System.out.println("API Gateway running... --- total time: " + (end-start));

    }
}
