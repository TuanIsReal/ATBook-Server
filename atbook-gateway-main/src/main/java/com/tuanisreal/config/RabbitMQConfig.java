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

    //Cache
    @Value("${application.rabbitmq.cache.exchangeName}")
    private String cacheExchangeName;

    @Value("${application.rabbitmq.cache.routingKey}")
    private String cacheRoutingKey;

    @Value("${application.rabbitmq.cache.queues.ums}")
    private String cacheUMSQueueName;

    //Context
    @Value("${application.rabbitmq.context.exchangeName}")
    private String contextExchangeName;

    //Action
    @Value("${application.rabbitmq.context.action.requestQueueName}")
    private String contextActionRequestQueueName;

    @Value("${application.rabbitmq.context.action.requestQueueRoutingKey}")
    private String contextActionRequestQueueRoutingKey;

    @Value("${application.rabbitmq.context.action.responseQueueName}")
    private String contextActionResponseQueueName;

    @Value("${application.rabbitmq.context.action.responseQueueRoutingKey}")
    private String contextActionResponseQueueRoutingKey;

    //AUTHENTICATION
    @Value("${application.rabbitmq.context.authentication.requestQueueName}")
    private String contextAuthenticationRequestQueueName;

    @Value("${application.rabbitmq.context.authentication.requestQueueRoutingKey}")
    private String contextAuthenticationRequestQueueRoutingKey;

    @Value("${application.rabbitmq.context.authentication.responseQueueName}")
    private String contextAuthenticationResponseQueueName;

    @Value("${application.rabbitmq.context.authentication.responseQueueRoutingKey}")
    private String contextAuthenticationResponseQueueRoutingKey;

    //User
    @Value("${application.rabbitmq.context.user.requestQueueName}")
    private String contextUserRequestQueueName;

    @Value("${application.rabbitmq.context.user.requestQueueRoutingKey}")
    private String contextUserRequestQueueRoutingKey;

    @Value("${application.rabbitmq.context.user.responseQueueName}")
    private String contextUserResponseQueueName;

    @Value("${application.rabbitmq.context.user.responseQueueRoutingKey}")
    private String contextUserResponseQueueRoutingKey;



}
