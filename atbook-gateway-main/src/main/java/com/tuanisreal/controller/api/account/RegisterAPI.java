package com.tuanisreal.controller.api.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.tuanisreal.constant.APIName;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.context.authentication.request.CreateTokenRequest;
import com.tuanisreal.context.authentication.response.CreateTokenResponse;
import com.tuanisreal.context.user.domain.User;
import com.tuanisreal.controller.UnauthenticatedAPI;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.request.account.RegisterUserRequest;
import com.tuanisreal.response.account.RegisterUserResponse;
import com.tuanisreal.utils.JsonUtil;

@Slf4j
@Component(APIName.REGISTER_USER)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterAPI extends UnauthenticatedAPI {

    private static final int INSERT_USER_STEP = 1;
    private static final int CREATE_TOKEN_STEP = 2;

    RegisterUserRequest requestAPI;
    User userResponse;
    CreateTokenResponse createTokenResponse;

    @Override
    protected void init() {
        addStartStep(INSERT_USER_STEP);
        addCondition(CREATE_TOKEN_STEP, INSERT_USER_STEP);
    }

    @Override
    protected Request parseRequest(String payload) {
        requestAPI = JsonUtil.toObject(payload, RegisterUserRequest.class);
        System.out.println(requestAPI.toString());
        return requestAPI;
    }

    @Override
    protected void saveResponseData(ContextResponse response) {
        switch (response.getStep()) {
            case INSERT_USER_STEP:
                userResponse = JsonUtil.toObject(response.getData(), User.class);
                break;
            case CREATE_TOKEN_STEP:
                createTokenResponse = JsonUtil.toObject(response.getData(), CreateTokenResponse.class);
                break;
        }
    }

    @Override
    protected void doStep(int step) {
        switch (step) {
            case INSERT_USER_STEP:
                insertUser();
                break;
            case CREATE_TOKEN_STEP:
                createToken();
                break;
        }
    }

    @Override
    protected Response createResponseData() {
        RegisterUserResponse responseData = new RegisterUserResponse(createTokenResponse.getToken(), createTokenResponse.getRefreshToken());
        return new Response(responseData);
    }

    private void insertUser() {
        sendRealTimeRequest(userPublisher, INSERT_USER_STEP, ActionName.INSERT_USER, requestAPI);
    }

    private void createToken() {
        CreateTokenRequest request = new CreateTokenRequest(userResponse.getUserId(), requestAPI.getApplication(), userResponse.getIsFinishRegister());
        sendRealTimeRequest(authenticationPublisher, CREATE_TOKEN_STEP, ActionName.CREATE_TOKEN, request);
    }

}
