package com.tuanisreal;

import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.config.RabbitMQConfig;
import com.tuanisreal.gateway.authentication.AuthenticationContextRequestContainer;
import com.tuanisreal.gateway.authentication.AuthenticationMessageQueuePublisher;
import com.tuanisreal.gateway.authentication.AuthenticationMessageQueueSubscriber;

@Slf4j
@Component
@AllArgsConstructor
public class MessageQueueManager {
    private final RabbitMQConfig rabbitMQConfig;

    private final AuthenticationMessageQueuePublisher authenticationMessageQueuePublisher;
    private final AuthenticationMessageQueueSubscriber authenticationMessageQueueSubscriber;

    public void initQueues(){
        ConnectionFactory factory = createConnectionFactory();

        initContextQueues(factory);
    }

    private ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQConfig.getHost());
        factory.setPort(rabbitMQConfig.getPort());
        factory.setUsername(rabbitMQConfig.getUsername());
        factory.setPassword(rabbitMQConfig.getPassword());
        return factory;
    }

    private void initContextQueues(ConnectionFactory factory) {
        authenticationMessageQueuePublisher.init("Authentication Publisher", rabbitMQConfig.getContextExchangeName(),
                rabbitMQConfig.getContextAuthenticationResponseQueueRoutingKey(), factory);
        authenticationMessageQueueSubscriber.init("Authentication Subscriber", rabbitMQConfig.getContextAuthenticationRequestQueueName(),
                factory, AuthenticationContextRequestContainer.getInstance());
    }
}
