package com.tuanisreal.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.context.authentication.domain.TokenElement;
import com.tuanisreal.context.authentication.request.CheckAuthenticationRequest;
import com.tuanisreal.context.authentication.response.CheckAuthenticationResponse;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.service.rs.base.SessionRSService;
import com.tuanisreal.service.ws.base.SessionWSService;
import com.tuanisreal.token.JWTCreator;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.utils.StringUtil;

import java.util.Objects;

@Slf4j
@Component("CHECK_AUTHENTICATION")
@AllArgsConstructor
public class CheckAuthenticationAPI implements APIHandler {

    @Override
    public Response execute(String requestPayload) {
        CheckAuthenticationRequest requestData = JsonUtil.toObject(requestPayload, CheckAuthenticationRequest.class);
        String token = requestData.getToken();
        if (!StringUtil.validateString(token)) {
            throw new ApplicationException(ResponseCode.INVALID_TOKEN);
        }

        TokenElement tokenElement = JWTCreator.getInstance().parseJWT(token);
        if (Objects.isNull(tokenElement)) {
            throw new ApplicationException(ResponseCode.INVALID_TOKEN);
        }

        CheckAuthenticationResponse responseData = CheckAuthenticationResponse.builder()
                .userId(tokenElement.getUserId())
                .sessionType(tokenElement.getSessionType())
                .usingApplication(tokenElement.getUsingApplication())
                .isFinishRegisterUser(tokenElement.isFinishRegisterUser())
                .build();

        return new Response(responseData);
    }

}
