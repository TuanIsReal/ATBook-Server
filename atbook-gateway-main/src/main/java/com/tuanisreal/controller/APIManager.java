package com.tuanisreal.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class APIManager {

    private final ApplicationContext context;

    public BaseAPI getAPI(String apiName) {
        try {
            return (BaseAPI) context.getBean(apiName);
        } catch (BeansException ex) {
            return null;
        }
    }
}
