package com.tuanisreal.worker.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import com.tuanisreal.controller.handler.ContextRequestHandler;
import com.tuanisreal.controller.request.ContextRequest;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@ManagedResource
public abstract class AbstractContextRequestContainer {
    protected static final int DEFAULT_CONTAINER_MAX_SIZE = 10;

    protected int threadNumber;
    protected int containerMaxSize = DEFAULT_CONTAINER_MAX_SIZE;

    protected final ConcurrentLinkedQueue<ContextRequest> CONTEXT_REQUEST_CONTAINER = new ConcurrentLinkedQueue<>();

    public void put(ContextRequest request) {
        log.debug("\nPush request {} to queue {}\n", request, this.getClass().getSimpleName());
        CONTEXT_REQUEST_CONTAINER.add(request);
    }

    public ContextRequest poll() {
        ContextRequest contextRequest = CONTEXT_REQUEST_CONTAINER.poll();
        return contextRequest;
    }

    public int size() {
        return CONTEXT_REQUEST_CONTAINER.size();
    }

    public void startProcessors(int threadNumber, ContextRequestHandler contextRequestHandler) {
        this.threadNumber = threadNumber;
        this.containerMaxSize = threadNumber * 2 > DEFAULT_CONTAINER_MAX_SIZE ? threadNumber * 2 : DEFAULT_CONTAINER_MAX_SIZE;
        for (int i = 0; i < threadNumber; i++) {
            ContextRequestProcessor contextRequestProcessor = new ContextRequestProcessor(this, contextRequestHandler);
            new Thread(contextRequestProcessor).start();
        }
    }

    @ManagedAttribute
    public int getSize() {
        return size();
    }
}
