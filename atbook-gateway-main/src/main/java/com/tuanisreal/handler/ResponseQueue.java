package com.tuanisreal.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@ManagedResource
public class ResponseQueue {
    private static final Queue<byte[]> CONTEXT_RESPONSE_QUEUE = new ConcurrentLinkedQueue<>();
    private static final ResponseQueue INSTANCE = new ResponseQueue();
    private int threadNumber;

    private ResponseQueue() {
    }

    public static ResponseQueue getInstance() {
        return INSTANCE;
    }

    public void init(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public void add(byte[] responseData) {
        CONTEXT_RESPONSE_QUEUE.add(responseData);
    }

    public byte[] poll() {
        return CONTEXT_RESPONSE_QUEUE.poll();
    }

    @ManagedAttribute
    public int getSize() {
        return CONTEXT_RESPONSE_QUEUE.size();
    }
}
