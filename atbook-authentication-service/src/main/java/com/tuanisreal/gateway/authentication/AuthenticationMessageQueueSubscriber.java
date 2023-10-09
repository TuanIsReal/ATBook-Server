package com.tuanisreal.gateway.authentication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.rbmq.BaseContextSubscriber;
import com.tuanisreal.utils.JsonUtil;

@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationMessageQueueSubscriber extends BaseContextSubscriber {

    @Override
    public void processMessage(byte[] body) {
        String message = new String(body);
        ContextRequest request = JsonUtil.toObject(message, ContextRequest.class);
        contextRequestContainer.put(request);
    }
}
