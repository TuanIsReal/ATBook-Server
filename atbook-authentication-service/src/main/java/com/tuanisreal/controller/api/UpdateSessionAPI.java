package com.tuanisreal.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.config.Config;
import com.tuanisreal.constant.Constant;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.context.authentication.domain.TokenElement;
import com.tuanisreal.context.authentication.request.UpdateSessionRequest;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.service.rs.base.SessionRSService;
import com.tuanisreal.token.JWTCreator;
import com.tuanisreal.utils.DateTimeUtil;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.utils.StringUtil;

import java.util.UUID;

@Slf4j
@Component("UPDATE_SESSION")
@AllArgsConstructor
public class UpdateSessionAPI implements APIHandler {
    private final Config config;
    private final SessionRSService sessionRSService;

    @Override
    public Response execute(String requestPayload) {
        UpdateSessionRequest requestData = JsonUtil.toObject(requestPayload, UpdateSessionRequest.class);
        String refreshToken = requestData.getRefreshToken();
        if (!StringUtil.validateString(refreshToken) || !sessionRSService.isExistedRefreshToken(refreshToken)) {
            log.debug("---Refresh token invalid---");
            throw new ApplicationException(ResponseCode.IN_VALID_REFRESH_TOKEN);
        }

        String userId = requestData.getUserId();
        String usingApplication = requestData.getUsingApplication();
        Boolean isFinishRegister = requestData.isFinishRegisterUser();

        long currentTime = DateTimeUtil.currentTime();
        TokenElement tokenElement = new TokenElement(userId, usingApplication, Constant.SESSION_TYPE.APPLICATION_USER, isFinishRegister);
        String newToken = JWTCreator.getInstance().generateJWT(tokenElement);
        String newRefreshToken = UUID.randomUUID().toString();
        Session newSession = Session.builder()
                .refreshToken(refreshToken)
                .userId(userId)
                .sessionType(Constant.SESSION_TYPE.APPLICATION_USER)
                .sessionExpire(currentTime + config.getSessionTimeout() * Constant.A_DAY)
                .usingApplication(usingApplication)
                .finishRegisterUser(isFinishRegister)
                .build();


        return null;
    }
}
