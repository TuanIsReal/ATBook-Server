package com.tuanisreal.service.rs.base;

import com.tuanisreal.context.authentication.domain.Session;

import java.util.List;

public interface SessionRSService {
    List<Session> getSessionsByUserId(String userId);
    boolean isExistedRefreshToken(String refreshToken);
    Session getSessionsByRefreshToken(String refreshToken);
}
