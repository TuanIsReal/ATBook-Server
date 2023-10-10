package com.tuanisreal.repository.base;

public interface UserSaltRepository {
    void insertUserSalt(String userId, String salt);
    String getSalt(String userId);
}
