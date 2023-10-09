package com.tuanisreal.controller.api;

import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("TEST_USER")
@AllArgsConstructor
public class TestUserApi implements APIHandler {
    @Override
    public Response execute(String requestPayload) {
        String response = "test ok";
        return new Response(response);
    }
}
