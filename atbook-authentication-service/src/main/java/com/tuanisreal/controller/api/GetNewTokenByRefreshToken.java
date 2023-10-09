package com.tuanisreal.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.config.Config;
import com.tuanisreal.constant.Constant;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.context.authentication.domain.TokenElement;
import com.tuanisreal.context.authentication.request.GetNewTokenByRefreshTokenRequest;
import com.tuanisreal.context.authentication.response.GetNewTokenByRefreshTokenResponse;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.service.rs.base.SessionRSService;
import com.tuanisreal.service.ws.base.SessionWSService;
import com.tuanisreal.token.JWTCreator;
import com.tuanisreal.utils.DateTimeUtil;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.utils.StringUtil;

@Slf4j
@Component("GET_NEW_TOKEN_BY_REFRESH_TOKEN")
@AllArgsConstructor
public class GetNewTokenByRefreshToken implements APIHandler {
    private final Config config;
    private final SessionRSService sessionRSService;
    private final SessionWSService sessionWSService;


    @Override
    public Response execute(String requestPayload) {
        GetNewTokenByRefreshTokenRequest requestData = JsonUtil.toObject(requestPayload, GetNewTokenByRefreshTokenRequest.class);
        String refreshToken = requestData.getRefreshToken();
        if (!StringUtil.validateString(refreshToken) || !sessionRSService.isExistedRefreshToken(refreshToken)) {
            log.debug("---Refresh token invalid---");
            throw new ApplicationException(ResponseCode.IN_VALID_REFRESH_TOKEN);
        }

        Session session = sessionRSService.getSessionsByRefreshToken(refreshToken);
        long timeExpired = DateTimeUtil.currentTime() - (config.getSessionTimeout() * Constant.A_DAY);
        if (session.getSessionExpire() < timeExpired){
            log.debug("---Refresh token invalid---");
            sessionWSService.removeSession(session.getRefreshToken());
            throw new ApplicationException(ResponseCode.IN_VALID_REFRESH_TOKEN);
        }
        TokenElement tokenElement = new TokenElement(session.getUserId(), session.getUsingApplication(), session.getSessionType(), session.isFinishRegisterUser());
        String token = JWTCreator.getInstance().generateJWT(tokenElement);

        GetNewTokenByRefreshTokenResponse responseData = new GetNewTokenByRefreshTokenResponse(token);
        return new Response(responseData);
    }
}
