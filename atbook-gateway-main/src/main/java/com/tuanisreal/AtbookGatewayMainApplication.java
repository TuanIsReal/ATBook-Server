package com.tuanisreal;

import com.tuanisreal.server.GatewayServer;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@AllArgsConstructor
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class AtbookGatewayMainApplication implements CommandLineRunner {

    private final GatewayServer gatewayServer;

    public static void main(String[] args) {
        SpringApplication.run(AtbookGatewayMainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        gatewayServer.start();
    }
}
