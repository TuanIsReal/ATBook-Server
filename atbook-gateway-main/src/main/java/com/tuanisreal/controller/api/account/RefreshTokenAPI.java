package com.tuanisreal.controller.api.account;

import com.tuanisreal.constant.APIName;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.context.authentication.request.GetNewTokenByRefreshTokenRequest;
import com.tuanisreal.context.authentication.response.GetNewTokenByRefreshTokenResponse;
import com.tuanisreal.controller.UnauthenticatedAPI;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.request.account.RefreshTokenRequest;
import com.tuanisreal.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component(APIName.REFRESH_TOKEN)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RefreshTokenAPI extends UnauthenticatedAPI {

    private static final int GET_NEW_TOKEN_STEP = 1;

    private RefreshTokenRequest requestAPI;
    private GetNewTokenByRefreshTokenResponse refreshTokenResponse;

    @Override
    protected void init() {
        addStartStep(GET_NEW_TOKEN_STEP);
    }

    @Override
    protected Request parseRequest(String payload) {
        requestAPI = JsonUtil.toObject(payload, RefreshTokenRequest.class);
        return requestAPI;
    }

    @Override
    protected void saveResponseData(ContextResponse response) {
        switch (response.getStep()) {
            case GET_NEW_TOKEN_STEP:
                refreshTokenResponse = JsonUtil.toObject(response.getData(), GetNewTokenByRefreshTokenResponse.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doStep(int step) {
        switch (step) {
            case GET_NEW_TOKEN_STEP:
                getTokenByRefreshToken();
                break;
            default:
                break;
        }
    }

    private void getTokenByRefreshToken() {
        GetNewTokenByRefreshTokenRequest request = new GetNewTokenByRefreshTokenRequest(requestAPI.getRefreshToken());
        sendRealTimeRequest(authenticationPublisher, GET_NEW_TOKEN_STEP, ActionName.GET_NEW_TOKEN_BY_REFRESH_TOKEN, request);
    }

    @Override
    protected Response createResponseData() {
        return new Response(refreshTokenResponse.getNewToken());
    }
}
