package com.tuanisreal.gateway.authentication;

import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.controller.APIManager;
import com.tuanisreal.controller.handler.APIHandler;
import com.tuanisreal.controller.handler.ContextRequestHandler;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class UserContextRequestHandler implements ContextRequestHandler {

    private final UserMessageQueuePublisher messageQueuePublisher;

    private final APIManager apiManager;

    @Override
    public void handle(ContextRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            String apiName = request.getActionName().toString();
            String payload = request.getPayload();

            APIHandler api = apiManager.getAPI(apiName);

            if (api == null) {
                sendResponse(request, ResponseCode.WRONG_DATA_FORMAT);
                return;
            }

            Response response = api.execute(payload);
            ContextResponse contextResponse = new ContextResponse(response.getCode(), response.getData());
            sendResponse(apiName, request, contextResponse);
            log.debug("Request {}:, action name: {}, payload: {}, response: {}",
                    request.getRequestId(), request.getActionName(),
                    request.getPayload(), JsonUtil.toJson(response));
        } catch (ApplicationException ex) {
            sendResponse(request, ex.getErrorCode());
        } catch (Exception ex) {
            log.error("Execute command error: ", ex);
            sendResponse(request, ResponseCode.UNKNOWN_ERROR);
        }
    }

    private void sendResponse(ContextRequest request, int code) {
        ContextResponse response = new ContextResponse(code);
        response.setRequestId(request.getRequestId());
        sendResponse(request, response);
        log.debug("Request {}:, action name: {}, payload: {}, response: {}",
                request.getRequestId(), request.getActionName(),
                request.getPayload(), JsonUtil.toJson(response));
    }

    private void sendResponse(String apiName, ContextRequest request, ContextResponse response) {
        sendResponse(request, response);
    }

    private void sendResponse(ContextRequest request, ContextResponse response) {
        messageQueuePublisher.sendResponse(response, request);
    }
}
