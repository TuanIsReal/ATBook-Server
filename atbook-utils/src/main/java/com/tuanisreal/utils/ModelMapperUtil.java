package com.tuanisreal.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Objects;

public class ModelMapperUtil {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    static {
        MODEL_MAPPER.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ModelMapperUtil() {
    }

    public static <T> T toObject(Object obj, Class<T> type) {
        T t = null;
        if (Objects.nonNull(obj)) {
            try {
                t = MODEL_MAPPER.map(obj, type);
            } catch (Exception ex) {
            }

        }
        return t;
    }

    public static void map(Object sourceObj, Object desObj) {
        if (Objects.nonNull(sourceObj) && Objects.nonNull(desObj)) {
            try {
                MODEL_MAPPER.map(sourceObj, desObj);
            } catch (Exception ex) {
            }
        }
    }
}
