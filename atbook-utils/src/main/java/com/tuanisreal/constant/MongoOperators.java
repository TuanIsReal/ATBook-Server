package com.tuanisreal.constant;

public class MongoOperators {
    public MongoOperators(){
        throw new IllegalStateException("Utility class");
    }
    public static final String EQUAL = "$eq";
    public static final String GREATER_THAN = "$gt";
    public static final String GREATER_THAN_EQUALS = "$gte";
    public static final String IN = "$in";
    public static final String LESS_THAN = "$lt";
    public static final String LESS_THAN_EQUALS = "$lte";
    public static final String NOT_EQUALS = "$ne";
    public static final String NOT_IN = "$nin";
    public static final String NOT = "$not";
    public static final String NOR = "$nor";
    public static final String OR = "$or";
    public static final String AND = "$and";
    public static final String EXISTS = "$exists";
    public static final String TYPE = "$lt";
    public static final String EXPR = "$expr";
    public static final String JSON_SCHEMA = "$jsonSchema";
    public static final String MOD = "$mod";
    public static final String REGEX = "regex";
    public static final String TEXT = "$text";
    public static final String WHERE = "$where";
    public static final String GEO_INTERSECTS = "$geoIntersects";
    public static final String INC = "$inc";
    public static final String SET = "$set";
    public static final String UNSET = "$unset";
    public static final String LAST = "$last";
    public static final String MATCH = "$match";
    public static final String GROUP = "$group";
    public static final String PROJECT = "$project";
    public static final String STATUS = "$status";
    public static final String SUM = "$sum";
    public static final String OPTIONS = "$options";
    public static final String ADD_TO_SET = "$addToSet";
    public static final String SIZE = "$size";
}
