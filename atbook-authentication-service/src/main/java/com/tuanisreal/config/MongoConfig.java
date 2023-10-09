package com.tuanisreal.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MongoConfig {
    @Value("${application.database.mongodb.uri}")
    private String connectionUri;

    @Bean
    public MongoClient mongoClient() {
        MongoClientURI mongoClientURI = new MongoClientURI(connectionUri);
        return new MongoClient(mongoClientURI);
    }
}
