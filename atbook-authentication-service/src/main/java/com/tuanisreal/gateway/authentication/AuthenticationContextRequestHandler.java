package com.tuanisreal.gateway.authentication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.constant.ParamKey;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.controller.APIManager;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.handler.ContextRequestHandler;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.utils.JsonUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationContextRequestHandler implements ContextRequestHandler {

    private final AuthenticationMessageQueuePublisher messageQueuePublisher;

    private final APIManager apiManager;

    @Override
    public void handle(ContextRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            String apiName = request.getActionName().toString();
            String payload = request.getPayload();

            APIHandler api = apiManager.getAPI(apiName);

            if (api == null) {
                sendResponse(request, ResponseCode.WRONG_DATA_FORMAT, startTime);
                return;
            }

            Response response = api.execute(payload);
            ContextResponse contextResponse = new ContextResponse(response.getCode(), response.getData());
            sendResponse(apiName, request, contextResponse, startTime);
            log.debug("Request {}:, action name: {}, payload: {}, response: {}",
                    request.getRequestId(), request.getActionName(),
                    request.getPayload(), JsonUtil.toJson(response));
        } catch (ApplicationException ex) {
            sendResponse(request, ex.getErrorCode(), startTime);
        } catch (Exception ex) {
            log.error("Execute command error: ", ex);
            sendResponse(request, ResponseCode.UNKNOWN_ERROR, startTime);
        }
    }

    private void sendResponse(ContextRequest request, int code, long startTime) {
        ContextResponse response = new ContextResponse(code);
        response.setRequestId(request.getRequestId());
        sendResponse(request, response, startTime);
        log.debug("Request {}:, action name: {}, payload: {}, response: {}",
                request.getRequestId(), request.getActionName(),
                request.getPayload(), JsonUtil.toJson(response));
    }

    private void sendResponse(String apiName, ContextRequest request, ContextResponse response, long startTime) {
        if (apiName.equals(ActionName.CHECK_AUTHENTICATION.toString())) {
            response.setCheckAuthenticationResult(true);
        }
        sendResponse(request, response, startTime);
    }

    private void sendResponse(ContextRequest request, ContextResponse response, long startTime) {
        try {
            long endTime = System.currentTimeMillis();
            // make log data
            Map<String, Object> logInfo = new HashMap<>();
            logInfo.put("service_name", "AUTHENTICATION");
            logInfo.put("api_name", request.getActionName().toString());
            logInfo.put("request_id", request.getRequestId());

            if (request.getPayload() != null && !request.getPayload().equals("")) {
                String userId = JsonUtil.getString(request.getPayload(), (ParamKey.USER_ID));
                if (userId != null) {
                    logInfo.put("user_id", userId);
                }
            }

            logInfo.put("start_time", new Date(startTime));
            logInfo.put("end_time", new Date(endTime));
            logInfo.put("process_time", endTime - startTime);
            logInfo.put("response_code", response.getCode());
            if (log.isDebugEnabled()) {
                logInfo.put("request_data", request.getPayload());
                logInfo.put("response_data", JsonUtil.toJson(response));
            }

            log.info("Authentication service send response: {}", logInfo);
        } catch (Exception ex) {
            log.error("Log data to elk error: ", ex);
        }
        messageQueuePublisher.sendResponse(response, request);
    }
}
