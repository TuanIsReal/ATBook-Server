package com.tuanisreal.service.base;

import com.tuanisreal.context.user.domain.User;
import com.tuanisreal.context.user.request.LoginUserRequest;

public interface UserService {
    User login(LoginUserRequest loginUserRequest);
    User insertUser(User user);
    boolean changeEmailPassword(String userId, String email, String newPassword);
}
