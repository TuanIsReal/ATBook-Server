package com.tuanisreal;

import com.tuanisreal.gateway.authentication.UserContextRequestContainer;
import com.tuanisreal.gateway.authentication.UserContextRequestHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RequestHandlerManager {
    private final UserContextRequestHandler userContextRequestHandler;

    public void startHandlers() {
        UserContextRequestContainer.getInstance().startProcessors(2, userContextRequestHandler);
    }
}
