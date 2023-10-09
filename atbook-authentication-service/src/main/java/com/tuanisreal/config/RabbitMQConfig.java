package com.tuanisreal.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitMQConfig {

    @Value("${application.rabbitmq.host}")
    private String host;

    @Value("${application.rabbitmq.port}")
    private int port;

    @Value("${application.rabbitmq.username}")
    private String username;

    @Value("${application.rabbitmq.password}")
    private String password;

    //Context
    @Value("${application.rabbitmq.context.exchangeName}")
    private String contextExchangeName;

    //AUTHENTICATION
    @Value("${application.rabbitmq.context.authentication.requestQueueName}")
    private String contextAuthenticationRequestQueueName;

    @Value("${application.rabbitmq.context.authentication.requestQueueRoutingKey}")
    private String contextAuthenticationRequestQueueRoutingKey;

    @Value("${application.rabbitmq.context.authentication.responseQueueName}")
    private String contextAuthenticationResponseQueueName;

    @Value("${application.rabbitmq.context.authentication.responseQueueRoutingKey}")
    private String contextAuthenticationResponseQueueRoutingKey;
}
