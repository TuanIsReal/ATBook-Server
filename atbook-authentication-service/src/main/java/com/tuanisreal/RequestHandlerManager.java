package com.tuanisreal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.tuanisreal.gateway.authentication.AuthenticationContextRequestContainer;
import com.tuanisreal.gateway.authentication.AuthenticationContextRequestHandler;

@Component
@AllArgsConstructor
public class RequestHandlerManager {
    private final AuthenticationContextRequestHandler authenticationContextRequestHandler;

    public void startHandlers() {
        AuthenticationContextRequestContainer.getInstance().startProcessors(2, authenticationContextRequestHandler);
    }
}
