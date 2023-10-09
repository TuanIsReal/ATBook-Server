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
import com.tuanisreal.context.user.request.LoginUserRequest;
import com.tuanisreal.controller.UnauthenticatedAPI;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.request.account.LoginRequest;
import com.tuanisreal.response.account.LoginResponse;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.utils.ModelMapperUtil;

@Slf4j
@Component(APIName.LOGIN)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginAPI extends UnauthenticatedAPI {

    private static final int LOGIN_USER_STEP = 1;
    private static final int CREATE_TOKEN_STEP = 2;

    protected LoginRequest requestAPI;
    private User userResponse;
    private CreateTokenResponse createTokenResponse;

    @Override
    protected void init() {
        addStartStep(LOGIN_USER_STEP);
        addCondition(CREATE_TOKEN_STEP, LOGIN_USER_STEP);
    }

    @Override
    protected Request parseRequest(String payload) {
        requestAPI = JsonUtil.toObject(payload, LoginRequest.class);
        return requestAPI;
    }

    @Override
    protected void saveResponseData(ContextResponse response) {
        switch (response.getStep()) {
            case LOGIN_USER_STEP:
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
            case LOGIN_USER_STEP:
                loginUser();
                break;
            case CREATE_TOKEN_STEP:
                createToken();
                break;
        }
    }

    @Override
    protected Response createResponseData() {
        String token = createTokenResponse.getToken();
        LoginResponse responseData = new LoginResponse(token);
        return new Response(responseData);
    }

    private void loginUser() {
        LoginUserRequest request = ModelMapperUtil.toObject(requestAPI, LoginUserRequest.class);
        sendRealTimeRequest(userPublisher, LOGIN_USER_STEP, ActionName.GET_USER_LOGIN, request);
    }


    private void createToken() {
        CreateTokenRequest request = new CreateTokenRequest(userResponse.getUserId(), requestAPI.getApplication(), userResponse.getIsFinishRegister());
        sendRealTimeRequest(authenticationPublisher, CREATE_TOKEN_STEP, ActionName.CREATE_TOKEN, request);
    }
}
