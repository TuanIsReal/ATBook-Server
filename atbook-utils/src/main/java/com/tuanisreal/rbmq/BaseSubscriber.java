package com.tuanisreal.rbmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class BaseSubscriber {
    protected String consumerName;
    protected String queueName;

    public void init(String consumerName, String queueName, ConnectionFactory factory) {
        try {
            this.consumerName = consumerName;
            this.queueName = queueName;
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            listen(channel);
        } catch (Exception ex) {
            log.error("Listen to queue {} failed: {}", queueName, ex.getMessage());
        }
    }

    private void listen(Channel channel) throws IOException {
        boolean autoAck = false;
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body){
                try {
                    long deliveryTag = envelope.getDeliveryTag();
                    processMessage(body);
                    channel.basicAck(deliveryTag, false);
                } catch (Exception ex) {
                    log.error("Handle message from queue error: {}", ex.getMessage());
                }
            }
        };
        channel.basicConsume(queueName, autoAck, consumer);
    }

    public abstract void processMessage(byte[] body);
}
