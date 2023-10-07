package com.tuanisreal.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ContextResponse {
    private String requestId;
    private int step;
    private int code = ResponseCode.SUCCESS;
    private String data;
    private boolean isCheckAuthenticationResult;

    @JsonIgnore
    private Map<String, Long> moreLogger = new HashMap<>();

    public ContextResponse(int code) {
        this.code = code;
    }

    public ContextResponse(Object data) {
        this.code = ResponseCode.SUCCESS;
        this.data = JsonUtil.toJson(data);
    }

    public ContextResponse(int code, Object data) {
        this.code = code;
        this.data = JsonUtil.toJson(data);
    }

    public void addLogData(Map<String, Long> logData) {
        if (Objects.isNull(logData)) {
            return;
        }
        moreLogger.putAll(logData);
    }
}
