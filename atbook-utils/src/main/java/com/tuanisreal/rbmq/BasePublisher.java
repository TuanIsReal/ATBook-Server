package com.tuanisreal.rbmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePublisher {
    private String publisherName;
    private String exchangeName;
    private String routingKey;
    private Channel channel;

    public void init(String publisherName, String exchangeName, String routingKey, ConnectionFactory factory) {
        try {
            this.publisherName = publisherName;
            this.exchangeName = exchangeName;
            this.routingKey = routingKey;
            Connection conn = factory.newConnection();
            this.channel = conn.createChannel();
        } catch (Exception ex) {
            log.error("Can not start publisher {}: {}", publisherName, ex.getMessage());
        }
    }

    public void publishMessage(String message) {
        try {
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
        } catch (Exception ex) {
            log.error("CHANEL : {}", channel == null);
            log.error("EXCHANGE NAME : {}", exchangeName == null);
            log.error("ROUTING KEY : {}", routingKey == null);
            log.error("MESSAGE : {}", message == null);
            log.error("Publisher : {}, failed to publish message ", publisherName, ex);
        }
    }

}
