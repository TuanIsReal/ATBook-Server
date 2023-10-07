package com.tuanisreal.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.tuanisreal.constant.ResponseCode;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response {
    String requestId;
    int code = ResponseCode.SUCCESS;
    Object data;

    @JsonIgnore
    private Map<String, Long> moreLogger = new HashMap<>();

    public Response(int code) {
        this.code = code;
    }

    public Response(Object data) {
        this.data = data;
    }
}
