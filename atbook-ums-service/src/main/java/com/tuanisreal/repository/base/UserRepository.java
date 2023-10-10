package com.tuanisreal.repository.base;

import com.tuanisreal.context.user.domain.User;

public interface UserRepository {
    User getUserByUserId(String userId);
    User getUserByEmail(String email);

    User insertUser(User user);
    void updateUserId(String userId);
    boolean checkEmailExist(String email);
    void changeEmailPassword(String userId, String email,String encryptedNewPassword, long time);
}
