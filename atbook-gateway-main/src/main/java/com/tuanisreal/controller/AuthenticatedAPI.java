package com.tuanisreal.controller;

import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.context.authentication.request.CheckAuthenticationRequest;
import com.tuanisreal.context.authentication.response.CheckAuthenticationResponse;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.utils.JsonUtil;

@Slf4j
public abstract class AuthenticatedAPI extends BaseAPI{

    private static final int CHECK_AUTHENTICATION_STEP = 0;

    protected Request basicRequestData;

    @Override
    public void handleRequest(String payload) {
        basicRequestData = parseRequest(payload);
        checkAuthentication(basicRequestData);
    }

    private void checkAuthentication(Request basicRequestData) {
        CheckAuthenticationRequest request = new CheckAuthenticationRequest(basicRequestData.getToken());
        authenticationPublisher.sendContextRequest(getRequestId(), CHECK_AUTHENTICATION_STEP, ActionName.CHECK_AUTHENTICATION, request);
    }

    @Override
    public void handleStepResponse(ContextResponse contextResponse) {
        if (contextResponse.getCode() != ResponseCode.SUCCESS) {
            handleStepError(contextResponse);
            return;
        }
        if (contextResponse.isCheckAuthenticationResult()) {
            handleAuthenticationResult(contextResponse);
            doStartSteps();
        } else {
            super.handleStepResponse(contextResponse);
        }
    }

    protected void handleAuthenticationResult(ContextResponse contextResponse) {
        CheckAuthenticationResponse responseData = JsonUtil.toObject(contextResponse.getData(), CheckAuthenticationResponse.class);
        setCommonParams(responseData, basicRequestData);
//        checkInvalidAccount(responseData, basicRequestData);
    }
    protected void setCommonParams(CheckAuthenticationResponse checkAuthenticationResponse, Request requestData) {
        requestData.setUserId(checkAuthenticationResponse.getUserId());
    }

}
