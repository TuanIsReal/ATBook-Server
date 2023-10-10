package com.tuanisreal.controller.api;

import com.tuanisreal.context.user.domain.User;
import com.tuanisreal.context.user.request.LoginUserRequest;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.service.base.UserService;
import com.tuanisreal.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("GET_USER_LOGIN")
@AllArgsConstructor
public class GetLoginUserAPI implements APIHandler {

    private final UserService userService;

    @Override
    public Response execute(String requestPayload) {
        LoginUserRequest requestData = JsonUtil.toObject(requestPayload, LoginUserRequest.class);
        User user = userService.login(requestData);
        return new Response(user);
    }
}
