package com.tuanisreal.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tuanisreal.cache.ControllerCache;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.controller.APIManager;
import com.tuanisreal.controller.BaseAPI;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.utils.JsonUtil;
import com.tuanisreal.utils.StringUtil;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("")
@AllArgsConstructor
@Slf4j
public class RequestHandler {

    private APIManager apiManager;

    @PostMapping
    private ResponseEntity<Response> handlerPostRequest(@RequestBody String payload){
        Request request = JsonUtil.toObject(payload, Request.class);
        String requestId = generateRequestId();
        request.setRequestId(requestId);
        Response response = handleRequest(requestId, request.getApiName(), payload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Response handleRequest(String requestId, String apiName, String payload){
        Response response = new Response(ResponseCode.WRONG_DATA_FORMAT);
        if (!StringUtil.validateString(apiName)){
            return response;
        }

        BaseAPI baseAPI = apiManager.getAPI(apiName);
        if (Objects.isNull(baseAPI)){
            return response;
        }

        baseAPI.setRequestId(requestId);
        ControllerCache.add(requestId, baseAPI);

        try {
            log.info("----Handler API: {}", apiName);
            baseAPI.handleRequest(payload);
            response = baseAPI.getResponseAPI();
        } catch (ApplicationException ex){
            return new Response(ex.getErrorCode());
        } catch (Exception ex) {
            log.info("----Error Handler API: {}", apiName);
            return new Response(ResponseCode.UNKNOWN_ERROR);
        }
        return response;
    }

    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
