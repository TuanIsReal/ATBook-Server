package com.tuanisreal.repository.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.Objects;

public abstract class BaseRepository {

    protected abstract DB getDatabase();

    protected abstract String getCollectionName();

    protected DBCollection getCollection() {
        return getDatabase().getCollection(getCollectionName());
    }

    protected void put(DBObject obj, String field, Object value) {
        if (Objects.nonNull(value)) {
            obj.put(field, value);
        }
    }
}
