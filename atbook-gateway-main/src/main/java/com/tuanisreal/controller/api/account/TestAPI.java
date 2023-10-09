package com.tuanisreal.controller.api.account;

import com.tuanisreal.constant.APIName;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.controller.UnauthenticatedAPI;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component(APIName.TEST)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestAPI extends UnauthenticatedAPI {

    private static final int TEST_USER_STEP = 1;
    Request requestAPI;

    String userResponse;

    @Override
    protected void init() {
        addStartStep(TEST_USER_STEP);
    }

    @Override
    protected Request parseRequest(String payload) {
        requestAPI = JsonUtil.toObject(payload, Request.class);
        return requestAPI;
    }

    @Override
    protected void saveResponseData(ContextResponse response) {
        switch (response.getStep()) {
            case TEST_USER_STEP:
                userResponse = JsonUtil.toObject(response.getData(), String.class);
                break;
        }
    }

    @Override
    protected void doStep(int step) {
        switch (step) {
            case TEST_USER_STEP:
                testUser();
                break;
        }
    }

    private void testUser() {
        sendRealTimeRequest(userPublisher, TEST_USER_STEP, ActionName.TEST_USER, requestAPI);
    }

    @Override
    protected Response createResponseData() {
        return new Response(userResponse);
    }
}
