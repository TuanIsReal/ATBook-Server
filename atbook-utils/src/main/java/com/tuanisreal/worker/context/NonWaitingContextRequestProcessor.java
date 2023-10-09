package com.tuanisreal.worker.context;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.controller.handler.ContextRequestHandler;
import com.tuanisreal.controller.request.ContextRequest;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.rbmq.BasePublisher;
import com.tuanisreal.utils.JsonUtil;

@Slf4j
@AllArgsConstructor
public class NonWaitingContextRequestProcessor implements Runnable{
    private final AbstractContextRequestContainer contextRequestContainer;
    private final ContextRequestHandler contextRequestHandler;
    private final BasePublisher basePublisher;

    @Override
    public void run() {
        while (true) {
            try {
                ContextRequest contextRequest = contextRequestContainer.poll();
                if (contextRequest != null) {
                    contextRequestHandler.handle(contextRequest);
                } else {
                    Thread.sleep(10);
                }
            } catch (Exception ex) {
                log.error("MessageRunner error: ", ex);
            }
        }
    }

    public void sendResponse(ContextRequest request) {
        ContextResponse response = new ContextResponse();
        response.setRequestId(request.getRequestId());
        response.setStep(request.getStep());
        basePublisher.publishMessage(JsonUtil.toJson(response));
    }
}
