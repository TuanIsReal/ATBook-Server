package com.tuanisreal.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.tuanisreal.config.MongoDBManager;
import com.tuanisreal.constant.MongoOperators;
import com.tuanisreal.context.user.domain.User;
import com.tuanisreal.repository.base.UserRepository;
import com.tuanisreal.repository.mongodb.BaseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Slf4j
@Repository
@AllArgsConstructor
public class UserRepositoryImpl extends BaseRepository implements UserRepository {

    private final MongoDBManager mongoDBManager;
    private final String COLLECTION_NAME = "user2";
    private static final String ID = "_id";
    private static final String EMAIL = "email";
    private static final String USER_ID = "user_id";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String GENDER = "gender";
    private static final String DATE_OF_BIRTH = "dob";
    private static final String ADDRESS = "address";
    private static final String LAST_LOGIN = "last_login";
    private static final String REGISTER_TIME = "reg_time";
    private static final String FINISH_REGISTER = "finish_reg";
    private static final String LAST_UPDATE = "lase_update";

    @Override
    protected DB getDatabase() {
        return mongoDBManager.getUserDatabase();
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public User getUserByUserId(String userId) {
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(ID, id);
        DBObject obj = getCollection().findOne(findObj);
        return castFromDbObject(obj);
    }

    @Override
    public User getUserByEmail(String email) {
        BasicDBObject query = new BasicDBObject(EMAIL, email);
        DBObject resultObj = getCollection().findOne(query);
        return castFromDbObject(resultObj);
    }

    @Override
    public User insertUser(User user) {
        DBObject obj = castFromEntity(user);

        getCollection().insert(obj, new WriteConcern(true));
        ObjectId id = (ObjectId) obj.get(ID);
        user.setUserId(id.toString());
        return user;
    }

    @Override
    public void updateUserId(String userId) {
        BasicDBObject query = new BasicDBObject(ID, new ObjectId(userId));
        BasicDBObject set = new BasicDBObject(MongoOperators.SET, new BasicDBObject(USER_ID, userId));
        getCollection().update(query, set);
    }

    @Override
    public boolean checkEmailExist(String email) {
        BasicDBObject query = new BasicDBObject();

        email = email.toLowerCase();
        query.append(EMAIL, email);
        DBObject resultObj = getCollection().findOne(query);
        return Objects.nonNull(resultObj);
    }

    @Override
    public void changeEmailPassword(String userId, String email, String encryptedNewPassword, long time) {
        ObjectId id = new ObjectId(userId);
        BasicDBObject query = new BasicDBObject(ID, id);

        BasicDBObject changePass = new BasicDBObject();

        if (Objects.nonNull(email) && Objects.nonNull(encryptedNewPassword)){
            put(changePass, EMAIL, email);
            put(changePass, PASSWORD, encryptedNewPassword);
            put(changePass, FINISH_REGISTER, true);
            put(changePass, LAST_UPDATE, time);
        }

        BasicDBObject updateObj = new BasicDBObject(MongoOperators.SET, changePass);
        getCollection().update(query, updateObj);
    }

    private User castFromDbObject(DBObject obj) {
        if (Objects.isNull(obj)){
            return null;
        }
        BasicDBObject basicObj = (BasicDBObject) obj;
        User user = new User();
        user.setUserId(basicObj.getString(USER_ID));
        user.setUsername(basicObj.getString(USERNAME));
        user.setEmail(basicObj.getString(EMAIL));
        user.setPassword(basicObj.getString(PASSWORD));
        user.setGender(basicObj.getInt(GENDER));
        user.setDateOfBirth(basicObj.getString(DATE_OF_BIRTH));
        user.setAddress(basicObj.getString(ADDRESS));
        user.setLastLogin(basicObj.getLong(LAST_LOGIN));
        user.setRegisterTime(basicObj.getLong(REGISTER_TIME));
        user.setIsFinishRegister(basicObj.getBoolean(FINISH_REGISTER));
        user.setLastUpdate(basicObj.getLong(LAST_UPDATE));
        return user;
    }

    private DBObject castFromEntity(User user){
        if (Objects.isNull(user)){
            return null;
        }
        DBObject dbObj = new BasicDBObject();
        if (Objects.nonNull(user.getEmail())){
            dbObj.put(EMAIL, user.getEmail());
        }
        if (Objects.nonNull(user.getPassword())){
            dbObj.put(PASSWORD, user.getPassword());
        }
        if (Objects.nonNull(user.getUsername())){
            dbObj.put(USERNAME, user.getUsername());
        }
        if (Objects.nonNull(user.getGender())){
            dbObj.put(GENDER, user.getGender());
        }
        if (Objects.nonNull(user.getDateOfBirth())){
            dbObj.put(DATE_OF_BIRTH, user.getDateOfBirth());
        }
        if (Objects.nonNull(user.getAddress())){
            dbObj.put(ADDRESS, user.getAddress());
        }
        if (Objects.nonNull(user.getLastLogin())){
            dbObj.put(LAST_LOGIN, user.getLastLogin());
        }
        if (Objects.nonNull(user.getRegisterTime())){
            dbObj.put(REGISTER_TIME, user.getRegisterTime());
        }
        if (Objects.nonNull(user.getIsFinishRegister())){
            dbObj.put(FINISH_REGISTER, user.getIsFinishRegister());
        }
        if (Objects.nonNull(user.getLastUpdate())){
            dbObj.put(LAST_UPDATE, user.getLastUpdate());
        }
        return dbObj;
    }
}
