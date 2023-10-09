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

    //UMS
    @Value("${application.rabbitmq.context.user.requestQueueName}")
    private String contextUserRequestQueueName;

    @Value("${application.rabbitmq.context.user.requestQueueRoutingKey}")
    private String contextUserRequestQueueRoutingKey;

    @Value("${application.rabbitmq.context.user.responseQueueName}")
    private String contextUserResponseQueueName;

    @Value("${application.rabbitmq.context.user.responseQueueRoutingKey}")
    private String contextUserResponseQueueRoutingKey;
}
