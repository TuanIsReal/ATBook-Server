package com.tuanisreal.gateway.authentication;

import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.rbmq.BaseContextSubscriber;
import com.tuanisreal.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserMessageQueueSubscriber extends BaseContextSubscriber {

    @Override
    public void processMessage(byte[] body) {
        String message = new String(body);
        ContextRequest request = JsonUtil.toObject(message, ContextRequest.class);
        contextRequestContainer.put(request);
    }
}
