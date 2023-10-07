package com.tuanisreal.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tuanisreal.constant.ParamKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @JsonProperty(ParamKey.API_NAME)
    protected String apiName;

    @JsonProperty(ParamKey.TOKEN)
    protected String token;

    @JsonProperty(ParamKey.APPLICATION)
    protected String application;

//    @JsonProperty(ParamKey.IP)
//    protected String ip;

    @JsonProperty(ParamKey.REQUEST_ID)
    protected String requestId;

    @JsonProperty(ParamKey.USER_ID)
    protected String userId;
}
