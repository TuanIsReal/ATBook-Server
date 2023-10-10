package com.tuanisreal.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.tuanisreal.config.MongoDBManager;
import com.tuanisreal.repository.base.UserSaltRepository;
import com.tuanisreal.repository.mongodb.BaseRepository;
import com.tuanisreal.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserSaltRepositoryImpl extends BaseRepository implements UserSaltRepository {
    private final MongoDBManager mongoDBManager;
    private final String COLLECTION_NAME = "user_salt";
    private static final String USER_ID = "user_id";
    private static final String SALT = "salt";
    private static final String LAST_UPDATE = "last_update";

    @Override
    protected DB getDatabase() {
        return mongoDBManager.getUserDatabase();
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public void insertUserSalt(String userId, String salt) {
        DBObject obj = new BasicDBObject();
        obj.put(USER_ID, userId);
        obj.put(SALT, salt);
        obj.put(LAST_UPDATE, DateTimeUtil.currentTime());
        getCollection().insert(obj);
    }

    @Override
    public String getSalt(String userId) {
        String result;
        BasicDBObject query = new BasicDBObject(USER_ID, userId);
        DBObject resultObj = getCollection().findOne(query);
        result = (String) resultObj.get(SALT);
        return result;
    }
}
