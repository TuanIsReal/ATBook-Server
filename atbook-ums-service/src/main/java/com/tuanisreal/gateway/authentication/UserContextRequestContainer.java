package com.tuanisreal.gateway.authentication;

import com.tuanisreal.worker.context.AbstractContextRequestContainer;

public class UserContextRequestContainer extends AbstractContextRequestContainer {
    private static final UserContextRequestContainer INSTANCE = new UserContextRequestContainer();

    private UserContextRequestContainer() {
    }

    public static UserContextRequestContainer getInstance() {
        return INSTANCE;
    }
}
