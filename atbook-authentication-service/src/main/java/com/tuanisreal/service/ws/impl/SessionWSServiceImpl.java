package com.tuanisreal.service.ws.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.tuanisreal.cache.SessionManager;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.repository.base.SessionRepository;
import com.tuanisreal.service.ws.base.SessionWSService;

@Service
@AllArgsConstructor
public class SessionWSServiceImpl implements SessionWSService {
    private final SessionRepository sessionRepository;
    @Override
    public void addSession(Session session, boolean saveToDB) {
        SessionManager.addSession(session);
        if (saveToDB){
            sessionRepository.insert(session);
        }
    }

    @Override
    public void removeSession(String refreshToken) {
        SessionManager.removeSession(refreshToken);
        sessionRepository.removeSession(refreshToken);
    }

    @Override
    public void updateSession(String oldRefreshToken, Session session) {
        SessionManager.removeSession(oldRefreshToken);
        sessionRepository.removeSession(oldRefreshToken);

        SessionManager.addSession(session);
        sessionRepository.insert(session);
    }

}
