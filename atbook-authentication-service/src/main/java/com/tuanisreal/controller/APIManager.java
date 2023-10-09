package com.tuanisreal.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.tuanisreal.controller.handler.APIHandler;

@Component
@AllArgsConstructor
public class APIManager {

    private final ApplicationContext context;

    public APIHandler getAPI(String apiName) {
        try{
            return (APIHandler) context.getBean(apiName);
        }catch(Exception ex){
            return null;
        }
    }
}
