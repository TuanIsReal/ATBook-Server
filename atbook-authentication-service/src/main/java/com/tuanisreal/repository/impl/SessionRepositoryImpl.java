package com.tuanisreal.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import com.tuanisreal.constant.MongoOperators;
import com.tuanisreal.context.authentication.domain.Session;
import com.tuanisreal.repository.MongoDBManager;
import com.tuanisreal.repository.base.SessionRepository;
import com.tuanisreal.repository.mongodb.BaseRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class SessionRepositoryImpl extends BaseRepository implements SessionRepository {

    private final MongoDBManager mongoDBManager;
    private static final String USER_SESSION_COLLECTION = "user_session";
    private static final String USER_ID = "user_id";
    private static final String REFRESH_TOKEN = "token";
    private static final String SESSION_TYPE = "type";
    private static final String USING_APPLICATION = "using_application";
    private static final String FINISH_REGISTER_USER = "finish_reg";
    private static final String SESSION_EXPIRE = "exp";

    @Override
    protected DB getDatabase() {
        return mongoDBManager.getUserDatabase();
    }

    @Override
    protected String getCollectionName() {
        return USER_SESSION_COLLECTION;
    }

    @Override
    public List<Session> getAll() {
        List<Session> result = new ArrayList<>();
        try (DBCursor cursor = getCollection().find()) {
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                Session session = parseFromDocument(obj);
                result.add(session);
            }
        }
        return result;
    }

    @Override
    public void insert(Session session) {
        if (Objects.isNull(session)){
            return;
        }

        DBObject obj = castFromEntity(session);
        getCollection().insert(obj);
    }

    @Override
    public Integer removeSessionExpiredTime(long sessionTimeOut) {
        Long timeExpired = new Date().getTime() - sessionTimeOut;
        BasicDBObject removeObj = new BasicDBObject(SESSION_EXPIRE, new BasicDBObject(MongoOperators.LESS_THAN, timeExpired));
        return getCollection().remove(removeObj).getN();
    }

    @Override
    public void removeSession(String refreshToken) {
        BasicDBObject removeObj = new BasicDBObject(REFRESH_TOKEN, refreshToken);
        getCollection().remove(removeObj);
    }

    private DBObject castFromEntity(Session session) {
        DBObject obj = new BasicDBObject();
        if (session == null) {
            return null;
        }
        put(obj, USER_ID, session.getUserId());
        put(obj, REFRESH_TOKEN, session.getRefreshToken());
        put(obj, USING_APPLICATION, session.getUsingApplication());
        put(obj, SESSION_TYPE, session.getSessionType());
        put(obj, FINISH_REGISTER_USER, session.isFinishRegisterUser());
        put(obj, SESSION_EXPIRE, session.getSessionExpire());
        return obj;
    }

    private Session parseFromDocument(BasicDBObject obj) {

        return Session.builder()
                .userId(obj.getString(USER_ID))
                .refreshToken(obj.getString(REFRESH_TOKEN))
                .usingApplication(obj.getString(USING_APPLICATION))
                .sessionType(obj.getInt(SESSION_TYPE))
                .finishRegisterUser(obj.getBoolean(FINISH_REGISTER_USER))
                .sessionExpire(obj.getLong(SESSION_EXPIRE))
                .build();

    }
}
