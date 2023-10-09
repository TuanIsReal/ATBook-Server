package com.tuanisreal.gateway.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.rbmq.BasePublisher;
import com.tuanisreal.utils.JsonUtil;

@Component
@Slf4j
public class AuthenticationMessageQueuePublisher extends BasePublisher {

    public void sendResponse(ContextResponse response, ContextRequest request) {
        response.setRequestId(request.getRequestId());
        response.setStep(request.getStep());
        String responseString = JsonUtil.toJson(response);
        publishMessage(responseString);
    }
}
