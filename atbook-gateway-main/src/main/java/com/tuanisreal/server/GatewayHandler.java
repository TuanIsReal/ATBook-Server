package com.tuanisreal.server;

import com.tuanisreal.util.HTTPUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class GatewayHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, HttpObject httpObject) throws Exception {
        try {
            String uri = getUri(httpObject);
        } catch (Exception ex) {
            HTTPUtil.sendInternalServerErrorResponse(context);
        }
    }
    private String getUri(HttpObject httpObject) {
        if (httpObject instanceof HttpRequest) {
            HttpRequest httpContent = (HttpRequest) httpObject;
            return httpContent.uri();
        }
        return "/";
    }

    private void handleRequest(String requestId, String apiName, String payload) {
        if (!StringUtil.validateString(apiName)) {
            HTTPUtil.sendBadRequestResponse(requestId);
            return;
        }
        BaseAPI baseController = apiManager.getAPI(apiName);
        if (baseController == null) {
            HTTPUtil.sendBadRequestResponse(requestId);
            return;
        }
        baseController.setRequestId(requestId);
        ControllerRepository.add(requestId, baseController);
        try {
            baseController.handleRequest(payload);
        } catch (ApplicationException ex) {
            Response response = new Response(ex.getErrorCode());
            if (ex.getErrorCode() == ResponseCode.INVALID_ACCOUNT) {
                response.setData(ex.getData());
            }
            HTTPUtil.sendResponse(requestId, response);
        } catch (Exception ex) {
            HTTPUtil.sendInternalServerErrorResponse(requestId);
        }
    }

    private void handleOtherAPI(ChannelHandlerContext context, HttpObject httpObject) {
        String ip = getClientIP(context, httpObject);
        if (ServiceHealthManager.isServerMaintaince(ip)) {
            sendMaintanceResponse(context);
            return;
        }
        String requestId = HttpRepository.generateRequestId();

        JsonObject jsonObject = getJsonBody(context, httpObject);
        if (jsonObject.isEmpty()) {
            return;
        }
        setClientIP(jsonObject, ip);
        setClientUserAgent(jsonObject, httpObject);
        String apiName = jsonObject.getString(ParamKey.API_NAME);

        NettyHttpData data = NettyHttpData.builder()
                .api(apiName)
                .context(context)
                .requestData(jsonObject.toString())
                .ip(ip)
                .startTime(System.currentTimeMillis())
                .build();
        HttpRepository.push(requestId, data);
        HTTPUtil.addSentryProperty(data, requestId);
        handleRequest(requestId, apiName, jsonObject.toString());
    }

    private JsonObject toJson(String json) {
        try {
            return new JsonObject(json);
        } catch (Exception ex) {
            return new JsonObject();
        }
    }

    private JsonObject getJsonBody(ChannelHandlerContext context, HttpObject httpObject) {
        JsonObject jsonObject = new JsonObject();
        if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) httpObject;

            ByteBuf content = httpContent.content();
            String json = content.toString(CharsetUtil.UTF_8);
            jsonObject = toJson(json);
        }
        return jsonObject;
    }

}
