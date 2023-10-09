package com.tuanisreal.gateway.authentication;

import com.tuanisreal.worker.context.AbstractContextRequestContainer;

public class AuthenticationContextRequestContainer extends AbstractContextRequestContainer {
    private static final AuthenticationContextRequestContainer INSTANCE = new AuthenticationContextRequestContainer();

    private AuthenticationContextRequestContainer() {
    }

    public static AuthenticationContextRequestContainer getInstance() {
        return INSTANCE;
    }
}
