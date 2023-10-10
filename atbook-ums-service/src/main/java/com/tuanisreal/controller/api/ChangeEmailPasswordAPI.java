package com.tuanisreal.controller.api;

import com.tuanisreal.context.user.request.ChangeEmailPasswordRequest;
import com.tuanisreal.context.user.response.ChangeEmailPasswordResponse;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.service.base.UserService;
import com.tuanisreal.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("CHANGE_EMAIL_PASSWORD")
@AllArgsConstructor
public class ChangeEmailPasswordAPI implements APIHandler {

    private final UserService userService;

    @Override
    public Response execute(String requestPayload) {
        ChangeEmailPasswordRequest requestData = JsonUtil.toObject(requestPayload, ChangeEmailPasswordRequest.class);
        boolean isChangeAccount = userService.changeEmailPassword(requestData.getUserId(), requestData.getEmail(), requestData.getNewPassword());
        ChangeEmailPasswordResponse response = new ChangeEmailPasswordResponse(isChangeAccount);
        return new Response(response);
    }
}
