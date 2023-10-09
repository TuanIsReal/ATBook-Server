package com.tuanisreal.repository.base;

import com.tuanisreal.context.authentication.domain.Session;

import java.util.List;

public interface SessionRepository {
    List<Session> getAll();
    void insert(Session session);
    Integer removeSessionExpiredTime(long sessionTimeOut);
    void removeSession(String refreshToken);
}
