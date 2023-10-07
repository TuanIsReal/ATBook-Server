package com.tuanisreal.rbmq.subscriber;

import com.tuanisreal.handler.ResponseQueue;
import com.tuanisreal.rbmq.BaseSubscriber;

public abstract class APIGatewaySubscriber extends BaseSubscriber {
    @Override
    public void processMessage(byte[] body) {
        ResponseQueue.getInstance().add(body);
    }
}
