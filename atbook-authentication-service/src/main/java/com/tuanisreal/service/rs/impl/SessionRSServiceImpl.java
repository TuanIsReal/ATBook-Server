package com.tuanisreal.service.rs.impl;

import org.springframework.stereotype.Service;
import com.tuanisreal.cache.SessionManager;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.service.rs.base.SessionRSService;
import com.tuanisreal.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionRSServiceImpl implements SessionRSService {

    @Override
    public List<Session> getSessionsByUserId(String userId) {
        if (!StringUtil.validateString(userId)) {
            return new ArrayList<>();
        }
        return SessionManager.getSessionsByUserId(userId);
    }

    @Override
    public boolean isExistedRefreshToken(String refreshToken) {
        return SessionManager.isExistedRefreshToken(refreshToken);
    }

    @Override
    public Session getSessionsByRefreshToken(String refreshToken) {
        return SessionManager.getSessionByRefreshToken(refreshToken);
    }
}
