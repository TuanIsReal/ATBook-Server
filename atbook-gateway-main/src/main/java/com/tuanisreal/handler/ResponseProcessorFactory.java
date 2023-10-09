package com.tuanisreal.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import com.tuanisreal.config.Config;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ResponseProcessorFactory {
    private static ResponseProcessorFactory factory = new ResponseProcessorFactory();

    private List<ResponseProcessor> listProcessor;

    public ResponseProcessorFactory getInstance() {
        return factory;
    }

    public void start() {
        ResponseQueue.getInstance().init(5);
        listProcessor = new ArrayList<>(5);
        for(int i = 0; i < 5; i++) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            listProcessor.add(responseProcessor);
            responseProcessor.start();
        }
    }

}
