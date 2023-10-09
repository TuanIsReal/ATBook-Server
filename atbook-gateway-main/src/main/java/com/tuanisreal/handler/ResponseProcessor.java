package com.tuanisreal.handler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.cache.ControllerCache;
import com.tuanisreal.controller.BaseAPI;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.exception.ApplicationException;
import com.tuanisreal.utils.JsonUtil;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProcessor extends Thread{

    private boolean isProcessing;

    @Override
    public void run() {
        while (true) {
            try {
                byte[] data = ResponseQueue.getInstance().poll();
                if (data != null) {
                    isProcessing = true;
                    ContextResponse contextResponse = JsonUtil.toObject(new String(data), ContextResponse.class);
                    handleResponse(contextResponse);
                } else {
                    isProcessing = false;
                    Thread.sleep(10);
                }
            } catch (Exception ex) {
                log.error("Error {}", ex);
            }
        }
    }

    private void handleResponse(ContextResponse contextResponse) {
        try {
            String requestId = contextResponse.getRequestId();
            if (requestId == null) {
                return;
            }
            BaseAPI baseAPI = ControllerCache.get(contextResponse.getRequestId());
            if (baseAPI != null) {
                baseAPI.handleStepResponse(contextResponse);
            }
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            log.info("Handle context response error", e);
        }
    }

    public boolean isProcessing() {
        return isProcessing;
    }
}
