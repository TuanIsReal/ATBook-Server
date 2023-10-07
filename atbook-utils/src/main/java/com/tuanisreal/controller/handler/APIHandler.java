package com.tuanisreal.controller.handler;

import com.tuanisreal.controller.response.Response;

public interface APIHandler {

    Response execute(String requestPayload);
}
