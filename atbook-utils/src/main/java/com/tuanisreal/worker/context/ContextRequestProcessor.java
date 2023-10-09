package com.tuanisreal.worker.context;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.controller.handler.ContextRequestHandler;
import com.tuanisreal.controller.request.ContextRequest;

@Slf4j
@AllArgsConstructor
public class ContextRequestProcessor implements Runnable{
    private final AbstractContextRequestContainer contextRequestContainer;
    private final ContextRequestHandler contextRequestHandler;

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
}
