package com.tuanisreal.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.config.Config;
import com.tuanisreal.constant.Constant;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.context.authentication.domain.TokenElement;
import com.tuanisreal.context.authentication.request.CreateTokenRequest;
import com.tuanisreal.context.authentication.response.CreateTokenResponse;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.service.ws.base.SessionWSService;
import com.tuanisreal.token.JWTCreator;
import com.tuanisreal.utils.DateTimeUtil;
import com.tuanisreal.utils.JsonUtil;

import java.util.UUID;

@Slf4j
@Component("CREATE_TOKEN")
@AllArgsConstructor
public class CreateTokenAPI implements APIHandler {
    private final Config config;

    private final SessionWSService sessionWSService;

    @Override
    public Response execute(String requestPayload) {
        CreateTokenRequest requestData = JsonUtil.toObject(requestPayload, CreateTokenRequest.class);
        String userId = requestData.getUserId();
        String usingApplication = requestData.getUsingApplication();
        Boolean isFinishRegister = requestData.isFinishRegisterUser();
        long currentTime = DateTimeUtil.currentTime();
        TokenElement tokenElement = new TokenElement(userId, usingApplication, Constant.SESSION_TYPE.APPLICATION_USER, isFinishRegister);
        String token = JWTCreator.getInstance().generateJWT(tokenElement);
        String refreshToken = UUID.randomUUID().toString();
        Session session = Session.builder()
                .refreshToken(refreshToken)
                .userId(userId)
                .sessionType(Constant.SESSION_TYPE.APPLICATION_USER)
                .sessionExpire(currentTime + config.getSessionTimeout() * Constant.A_DAY)
                .usingApplication(usingApplication)
                .finishRegisterUser(isFinishRegister)
                .build();

        sessionWSService.addSession(session, true);
        CreateTokenResponse responseData = new CreateTokenResponse(token, refreshToken);
        return new Response(responseData);
    }
}
