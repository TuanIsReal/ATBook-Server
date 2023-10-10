package com.tuanisreal.config;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.tuanisreal.constant.DatabaseName;

@Configuration
@AllArgsConstructor
public class MongoDBManager {

    private final MongoClient mongoClient;

    @Bean
    public DB getUserDatabase() {
        return mongoClient.getDB(DatabaseName.USER_DB);
    }
}
