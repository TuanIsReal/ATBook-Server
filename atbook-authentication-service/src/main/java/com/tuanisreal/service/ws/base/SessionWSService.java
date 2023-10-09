package com.tuanisreal.service.ws.base;

import com.tuanisreal.context.authentication.domain.Session;

public interface SessionWSService {
    void addSession(Session session, boolean saveToDB);
    void removeSession(String refreshToken);
    void updateSession(String oldRefreshToken, Session session);
}
