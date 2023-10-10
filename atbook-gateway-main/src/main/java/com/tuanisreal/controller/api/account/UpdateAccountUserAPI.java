package com.tuanisreal.controller.api.account;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.tuanisreal.constant.APIName;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.context.authentication.response.CreateTokenResponse;
import com.tuanisreal.context.user.request.ChangeEmailPasswordRequest;
import com.tuanisreal.context.user.response.ChangeEmailPasswordResponse;
import com.tuanisreal.controller.AuthenticatedAPI;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.request.account.UpdateAccountUserRequest;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.utils.ModelMapperUtil;

@Component(APIName.UPDATE_ACCOUNT_USER)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateAccountUserAPI extends AuthenticatedAPI {

    private static final int CHANGE_EMAIL_PASSWORD_STEP = 1;
    private static final int CREATE_TOKEN_STEP = 2;

    private UpdateAccountUserRequest requestAPI;
    private ChangeEmailPasswordResponse changeEmailPasswordResponse;
    CreateTokenResponse createTokenResponse;

    @Override
    protected void init() {
        addStartStep(CHANGE_EMAIL_PASSWORD_STEP);
    }

    @Override
    protected Request parseRequest(String payload) {
        requestAPI = JsonUtil.toObject(payload, UpdateAccountUserRequest.class);
        return requestAPI;
    }

    @Override
    protected void saveResponseData(ContextResponse response) {
        switch (response.getStep()) {
            case CHANGE_EMAIL_PASSWORD_STEP:
                changeEmailPasswordResponse = JsonUtil.toObject(response.getData(), ChangeEmailPasswordResponse.class);
                break;
            case CREATE_TOKEN_STEP:
                createTokenResponse = JsonUtil.toObject(response.getData(), CreateTokenResponse.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doStep(int step) {
        switch (step) {
            case CHANGE_EMAIL_PASSWORD_STEP:
                changeEmailPassword();
                break;
            default:
                break;
        }
    }

    private void changeEmailPassword() {
        ChangeEmailPasswordRequest request = ModelMapperUtil.toObject(requestAPI, ChangeEmailPasswordRequest.class);
        sendRealTimeRequest(userPublisher, CHANGE_EMAIL_PASSWORD_STEP, ActionName.CHANGE_EMAIL_PASSWORD, request);
    }

    @Override
    protected Response createResponseData() {
        return new Response(changeEmailPasswordResponse);
    }
}
