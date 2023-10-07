package com.tuanisreal.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.exception.JsonParserException;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String toJson(Object object) {
        String json = "";
        if (Objects.nonNull(object)) {
            try {
                json = MAPPER.writeValueAsString(object);
            } catch (JsonProcessingException ex) {
                log.error("Error convert object to json", ex);
            }
        }
        return json;
    }

    public static <T> T toObject(String json, Class<T> type) throws JsonParserException {
        T result = null;
        if (StringUtil.validateString(json)) {
            try {
                result = MAPPER.readValue(json, type);
            } catch (IOException ex) {
                log.error("To object exception", ex);
                throw new JsonParserException("Cannot convert Json to Object type " + type.toString(), ex);
            }

        }
        return result;
    }

    public static String getString(String json, String attribute) {
        String result = null;
        try {
            ObjectNode jsonObj = MAPPER.readValue(json, ObjectNode.class);
            JsonNode value = jsonObj.get(attribute);
            if (value != null) {
                result = value.asText();
            }
        } catch (IOException ex) {
            throw new JsonParserException("Cannot convert Json to Object type " + ObjectNode.class.toString(), ex);
        }
        return result;
    }

}
