package com.tuanisreal.worker.context;

import com.tuanisreal.controller.handler.ContextRequestHandler;
import com.tuanisreal.rbmq.BasePublisher;

public abstract class AbstractNonWaitingContextRequestContainer extends AbstractContextRequestContainer{
    @Override
    @Deprecated
    public void startProcessors(int threadNumber, ContextRequestHandler contextRequestHandler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void startProcessors(int threadNumber, ContextRequestHandler contextRequestHandler, BasePublisher publisher) {
        this.threadNumber = threadNumber;
        this.containerMaxSize = threadNumber * 2 > DEFAULT_CONTAINER_MAX_SIZE ? threadNumber * 2 : DEFAULT_CONTAINER_MAX_SIZE;
        for (int i = 0; i < threadNumber; i++) {
            NonWaitingContextRequestProcessor contextRequestProcessor = new NonWaitingContextRequestProcessor(this, contextRequestHandler, publisher);
            new Thread(contextRequestProcessor).start();
        }
    }
}
