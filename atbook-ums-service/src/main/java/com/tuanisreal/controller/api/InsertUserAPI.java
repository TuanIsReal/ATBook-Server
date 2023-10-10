package com.tuanisreal.controller.api;

import com.tuanisreal.context.user.domain.User;
import com.tuanisreal.context.user.request.InsertUserRequest;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.service.base.UserService;
import com.tuanisreal.utils.DateTimeUtil;
import com.tuanisreal.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("INSERT_USER")
@AllArgsConstructor
public class InsertUserAPI implements APIHandler {

    private final UserService userService;

    @Override
    public Response execute(String requestPayload) {
        InsertUserRequest requestData = JsonUtil.toObject(requestPayload, InsertUserRequest.class);
        Long currentTime = DateTimeUtil.currentTime();
        User user = User.builder()
                .username(requestData.getUsername())
                .gender(requestData.getGender())
                .dateOfBirth(requestData.getDateOfBirth())
                .address(requestData.getAddress())
                .lastLogin(currentTime)
                .registerTime(currentTime)
                .isFinishRegister(false)
                .build();
        user = userService.insertUser(user);
        return new Response(user);
    }
}
