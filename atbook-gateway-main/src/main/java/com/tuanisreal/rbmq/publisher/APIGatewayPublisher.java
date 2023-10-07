package com.tuanisreal.rbmq.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.rbmq.BasePublisher;
import com.tuanisreal.utils.JsonUtil;

@Slf4j
@Component
public abstract class APIGatewayPublisher extends BasePublisher {
    public void sendContextRequest(String requestId, int step, ActionName actionName, Object requestData) {
        ContextRequest request = new ContextRequest(requestId, step, actionName, JsonUtil.toJson(requestData));
        String message = JsonUtil.toJson(request);
        publishMessage(message);
        log.info("SEND WAITING REQUEST : {}", message);
    }

    public void sendNonWaitingContextRequest(String requestId, int step, ActionName actionName, Object requestData) {
        ContextRequest request = new ContextRequest(requestId, step, actionName, JsonUtil.toJson(requestData));
        request.setNonWaitingResponseRequest(true);
        String message = JsonUtil.toJson(request);
        publishMessage(message);
        log.info("SEND NON WAITING REQUEST : {}", message);
    }

}
