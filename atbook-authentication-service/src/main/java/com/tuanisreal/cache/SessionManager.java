package com.tuanisreal.cache;

import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.config.Config;
import com.tuanisreal.constant.Constant;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.repository.base.SessionRepository;
import com.tuanisreal.utils.CollectionUtil;
import com.tuanisreal.utils.DateTimeUtil;
import com.tuanisreal.utils.StringUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class SessionManager {
    public static Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    public static Map<String, List<Session>> USER_SESSION_MAP = new ConcurrentHashMap<>();

    private static Config xConfig;
    private static SessionRepository xSessionRepository;

    public static void init(Config config, SessionRepository sessionRepository) {
        xConfig = config;
        xSessionRepository = sessionRepository;
        removeSessionExpiredTime();
    }

    private static void removeSessionExpiredTime() {
        Integer removed = xSessionRepository.removeSessionExpiredTime(xConfig.getSessionTimeout() * Constant.A_DAY);
        List<Session> sessions = xSessionRepository.getAll();
        sessions.forEach(session -> addSessionFromDB(session));
    }

    private static Session addSessionFromDB(Session session) {
        List<Session> listSession = null;
        long currentTime = DateTimeUtil.currentTime();
        session.setSessionExpire(currentTime + xConfig.getSessionTimeout() * Constant.A_MINUTE);
        if (USER_SESSION_MAP.containsKey(session.getUserId())) {
            listSession = USER_SESSION_MAP.get(session.getUserId());
            listSession.add(session);
        } else {
            listSession = new ArrayList<>();
            listSession.add(session);
        }
        USER_SESSION_MAP.put(session.getUserId(), listSession);
        return SESSION_MAP.put(session.getRefreshToken(), session);
    }

    public static boolean isExistedRefreshToken (String refreshToken) {
        return SESSION_MAP.containsKey(refreshToken);
    }

    public static Session getSessionByRefreshToken(String refreshToken) {
        return SESSION_MAP.get(refreshToken);
    }

    public static Session addSession(Session session) {
        List<Session> listSession = null;
        if (USER_SESSION_MAP.containsKey(session.getUserId())) {
            listSession = USER_SESSION_MAP.get(session.getUserId());
            listSession.add(session);
        } else {
            listSession = new ArrayList<>();
            listSession.add(session);
        }
        USER_SESSION_MAP.put(session.getUserId(), listSession);
        return SESSION_MAP.put(session.getRefreshToken(), session);
    }

    public static List<Session> getSessionsByUserId(String userId) {
        if (!StringUtil.validateString(userId)) {
            return Collections.emptyList();
        }
        List<Session> result = USER_SESSION_MAP.get(userId);
        if(CollectionUtil.isEmpty(result)){
            return Collections.emptyList();
        }
        return result;
    }

    public static Session removeSession(String refreshToken){
        Session session = SESSION_MAP.get(refreshToken);
        if (Objects.nonNull(session)){
            String userId = session.getUserId();
            List<Session> sessionList = USER_SESSION_MAP.get(userId);
            List<Session> newSessionList = sessionList.stream()
                    .filter(session1 -> !refreshToken.equals(session1.getRefreshToken()))
                    .collect(Collectors.toList());
            USER_SESSION_MAP.put(userId, newSessionList);
        }
        return SESSION_MAP.remove(refreshToken);
    }

}
