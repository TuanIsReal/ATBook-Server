package com.tuanisreal.util;

import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.vertx.core.http.HttpServerResponse;

public class HTTPUtil {
    private HTTPUtil() {
    }

    public static void sendInternalServerErrorResponse(String requestId) {
        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
        sendResponse(requestId, response);
    }

//    public static void sendInternalServerErrorResponseWithLog(String requestId, BaseAPI api) {
//        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
//        sendResponse(requestId, response, api);
//    }

//    public static void sendInternalServerErrorResponse(HttpServerResponse httpServerResponse) {
//        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
//        sendResponse(httpServerResponse, JsonUtil.toJson(response));
//    }
//
//    public static void sendInternalServerErrorResponse(ChannelHandlerContext context) {
//        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
//        sendResponse(context, JsonUtil.toJson(response));
//    }

    public static void sendBadRequestResponse(String requestId) {
        Response response = new Response(ResponseCode.WRONG_DATA_FORMAT);
        sendResponse(requestId, response);
    }

    public static void sendResponse(String requestId, Response response) {
        try {
            NettyHttpData data = HttpRepository.pull(requestId);
            if (data != null) {
                ChannelHandlerContext context = data.getContext();
                String jsonResponse = JsonUtils.toJson(response);
                sendResponse(context, jsonResponse);
                long now = System.currentTimeMillis();
                long processTime = (now - data.getStartTime());
                if (processTime > 3000) {
                    try {
                        GatewayLogData logData = new GatewayLogData(data, response, processTime, requestId);
                        logService.log(logData.toMap());
                    } catch (Exception ex) {
                    }

                }
            }
        } catch (Exception ex) {
        } finally {
            HttpRepository.pull(requestId);
            ControllerRepository.remove(requestId);
        }
    }

    public static void sendResponse(String requestId, Response response, BaseAPI api) {
        try {
            NettyHttpData data = HttpRepository.pull(requestId);
            if (data != null) {
                int responseCode = response.getCode();
                ChannelHandlerContext context = data.getContext();
                String jsonResponse = JsonUtils.toJson(response);
                sendResponse(context, jsonResponse);
                long now = System.currentTimeMillis();
                long processTime = (now - data.getStartTime());
                if (processTime > 3000) {
                }

                try {
                    GatewayLogData logData = new GatewayLogData(data, response, processTime, requestId, api);
                    logService.log(logData.toMap());
                } catch (Exception ex){
                }
            }
        } catch (Exception ex) {
        } finally {
            HttpRepository.pull(requestId);
            ControllerRepository.remove(requestId);
        }
    }
}
