package com.tuanisreal.rbmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.config.RabbitMQConfig;
import com.tuanisreal.rbmq.publisher.api.ActionPublisher;
import com.tuanisreal.rbmq.publisher.api.AuthenticationPublisher;
import com.tuanisreal.rbmq.publisher.api.UserPublisher;
import com.tuanisreal.rbmq.publisher.cache.ResetCachePublisher;
import com.tuanisreal.rbmq.subscriber.api.ActionSubscriber;
import com.tuanisreal.rbmq.subscriber.api.AuthenticationSubscriber;
import com.tuanisreal.rbmq.subscriber.api.UserSubscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@AllArgsConstructor
public class MessageQueueManager {
    private final RabbitMQConfig rabbitMQConfig;

    private final ActionPublisher actionPublisher;
    private final ActionSubscriber actionSubscriber;

    private final AuthenticationPublisher authenticationPublisher;
    private final AuthenticationSubscriber authenticationSubscriber;

    private final ResetCachePublisher cachePublisher;

    private final UserSubscriber userSubscriber;
    private final UserPublisher userPublisher;



    public void createQueuesAndStart() throws IOException, TimeoutException {
        Channel channel = declareExchanges();
        log.info("Declare all exchange done");
        declareQueues(channel);
        log.info("Declare all queue done");
        initPublishers();
        initSubscribers();
    }

    private Channel declareExchanges() throws IOException, TimeoutException {
        Connection connection = createConnectionFactory().newConnection();
        Channel channel = connection.createChannel();

        channel = declareExchange(connection, channel, rabbitMQConfig.getContextExchangeName());
        channel = declareExchange(connection, channel, rabbitMQConfig.getCacheExchangeName());
        return channel;
    }

    private void declareQueues(Channel channel) throws IOException, TimeoutException {
        Connection connection = createConnectionFactory().newConnection();
        channel = declareContextQueues(connection, channel);
        declareCacheQueues(connection, channel);
    }

    private Channel declareExchange(Connection connection, Channel channel, String exchangeName) throws IOException {
        if (!isExchangeExisted(channel, exchangeName)) {
            channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            log.info("Exchange {} is declared.", exchangeName);
        } else {
            log.info("Exchange {} isn't declared because it has existed.", exchangeName);

        }
        return channel;
    }

    private Channel declareContextQueues(Connection connection, Channel channel) throws IOException{
        //Action
        channel = declareQueue(connection, channel, rabbitMQConfig.getContextActionRequestQueueName(),
                rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextActionRequestQueueRoutingKey());
        channel = declareQueue(connection, channel, rabbitMQConfig.getContextActionResponseQueueName(),
                rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextActionResponseQueueRoutingKey());

        //authentication
        channel = declareQueue(connection, channel, rabbitMQConfig.getContextAuthenticationRequestQueueName(),
                rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextAuthenticationRequestQueueRoutingKey());
        channel = declareQueue(connection, channel, rabbitMQConfig.getContextAuthenticationResponseQueueName(),
                rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextAuthenticationResponseQueueRoutingKey());

        //User
        channel = declareQueue(connection, channel, rabbitMQConfig.getContextUserRequestQueueName(),
                rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextUserRequestQueueRoutingKey());
        channel = declareQueue(connection, channel, rabbitMQConfig.getContextUserResponseQueueName(),
                rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextUserResponseQueueRoutingKey());

        return channel;
    }

    private Channel declareCacheQueues(Connection connection, Channel channel) throws IOException{
        channel = declareQueue(connection, channel, rabbitMQConfig.getCacheUMSQueueName(),
                rabbitMQConfig.getCacheExchangeName(), rabbitMQConfig.getCacheRoutingKey());

        return channel;
    }

    private Channel declareQueue(Connection connection, Channel channel, String queueName, String exchangeName, String routingKey) throws IOException {
        Map<String, Object> args = new HashMap<>();

        if (!isQueueExisted(channel, queueName)) {
            channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, args);
        } else {
            channel.queuePurge(queueName);
        }
        channel.queueBind(queueName, exchangeName, routingKey);
        return channel;
    }

    private boolean isQueueExisted(Channel channel, String queueName) {
        try {
            channel.queueDeclarePassive(queueName);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    private boolean isExchangeExisted(Channel channel, String exchangeName) {
        try {
            channel.exchangeDeclarePassive(exchangeName);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    private ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQConfig.getHost());
        factory.setPort(rabbitMQConfig.getPort());
        factory.setUsername(rabbitMQConfig.getUsername());
        factory.setPassword(rabbitMQConfig.getPassword());

        return factory;
    }

    private void initPublishers() {
        ConnectionFactory factory = createConnectionFactory();
        initContextPublishers(factory);
        startCachePublisher(factory);
    }

    private void initContextPublishers(ConnectionFactory factory) {
        actionPublisher.init("Action publisher", rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextActionRequestQueueRoutingKey(), factory);
        authenticationPublisher.init("Authentication publisher", rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextAuthenticationRequestQueueRoutingKey(), factory);
        userPublisher.init("User publisher", rabbitMQConfig.getContextExchangeName(), rabbitMQConfig.getContextUserRequestQueueRoutingKey(), factory);
    }

    private void startCachePublisher(ConnectionFactory factory) {
        cachePublisher.init("Cache publisher", rabbitMQConfig.getCacheExchangeName(), rabbitMQConfig.getCacheRoutingKey(), factory);
    }

    private void initSubscribers() {
        ConnectionFactory factory = createConnectionFactory();
        initContextSubscribers(factory);
    }

    private void initContextSubscribers(ConnectionFactory factory) {
        actionSubscriber.init("Action subscriber", rabbitMQConfig.getContextActionResponseQueueName(), factory);
        authenticationSubscriber.init("Authentication subscriber", rabbitMQConfig.getContextAuthenticationResponseQueueName(), factory);
        userSubscriber.init("User subscriber", rabbitMQConfig.getContextUserResponseQueueName(), factory);
    }
}
