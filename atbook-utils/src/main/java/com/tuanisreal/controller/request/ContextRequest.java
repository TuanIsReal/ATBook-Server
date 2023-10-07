package com.tuanisreal.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tuanisreal.constant.ActionName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContextRequest {
    private String requestId;
    private int step;
    private boolean isNonWaitingResponseRequest;
    private ActionName actionName;
    private String payload;
    private Long startTime;

    public ContextRequest(String requestId, int step, ActionName actionName, String payload) {
        this.requestId = requestId;
        this.step = step;
        this.actionName = actionName;
        this.payload = payload;
    }
}
