package com.tuanisreal;

import com.rabbitmq.client.ConnectionFactory;
import com.tuanisreal.config.RabbitMQConfig;
import com.tuanisreal.gateway.authentication.UserContextRequestContainer;
import com.tuanisreal.gateway.authentication.UserMessageQueuePublisher;
import com.tuanisreal.gateway.authentication.UserMessageQueueSubscriber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MessageQueueManager {
    private final RabbitMQConfig rabbitMQConfig;

    private final UserMessageQueuePublisher userMessageQueuePublisher;
    private final UserMessageQueueSubscriber userMessageQueueSubscriber;

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
        userMessageQueuePublisher.init("User Publisher", rabbitMQConfig.getContextExchangeName(),
                rabbitMQConfig.getContextUserResponseQueueRoutingKey(), factory);
        userMessageQueueSubscriber.init("User Subscriber", rabbitMQConfig.getContextUserRequestQueueName(),
                factory, UserContextRequestContainer.getInstance());
    }
}
