package com.tuanisreal.rbmq;

import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.worker.context.AbstractContextRequestContainer;

@Slf4j
public abstract class BaseContextSubscriber extends BaseSubscriber{
    protected AbstractContextRequestContainer contextRequestContainer;

    public void init(String consumerName, String queueName, ConnectionFactory factory, AbstractContextRequestContainer contextRequestContainer) {
        this.contextRequestContainer = contextRequestContainer;
        init(consumerName, queueName, factory);
    }
    @Override
    public void processMessage(byte[] body) {
        String message = new String(body);
        log.info("REQUEST FROM QUEUE: {}", message);
        ContextRequest request = JsonUtil.toObject(message, ContextRequest.class);
        contextRequestContainer.put(request);
    }
}
