package com.tuanisreal.util;

import io.netty.channel.ChannelHandlerContext;
import io.vertx.core.http.HttpServerResponse;

public class HTTPUtil {
    private HTTPUtil() {
    }

    public static void sendServerMaintainceResponse(HttpServerResponse httpServerResponse, MaintainceInfoResponse responseData) {
        Response response = new Response(ResponseCode.SERVER_MAINTAIN, responseData);
        String jsonResponse = JsonUtils.toJson(response);
        sendResponse(httpServerResponse, jsonResponse);
    }

    public static void sendServerMaintainceResponse(ChannelHandlerContext context, MaintainceInfoResponse responseData) {
        Response response = new Response(ResponseCode.SERVER_MAINTAIN, responseData);
        String jsonResponse = JsonUtils.toJson(response);
        sendResponse(context, jsonResponse);
    }

    public static void sendInternalServerErrorResponse(String requestId) {
        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
        sendResponse(requestId, response);
    }

    public static void sendInternalServerErrorResponseWithLog(String requestId, BaseAPI api) {
        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
        sendResponse(requestId, response, api);
    }

    public static void sendInternalServerErrorResponse(HttpServerResponse httpServerResponse) {
        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
        sendResponse(httpServerResponse, JsonUtils.toJson(response));
    }

    public static void sendInternalServerErrorResponse(ChannelHandlerContext context) {
        Response response = new Response(ResponseCode.UNKNOWN_ERROR);
        sendResponse(context, JsonUtils.toJson(response));
    }

    public static void sendBadRequestResponse(String requestId) {
        Response response = new Response(ResponseCode.WRONG_DATA_FORMAT);
        sendResponse(requestId, response);
    }
}
