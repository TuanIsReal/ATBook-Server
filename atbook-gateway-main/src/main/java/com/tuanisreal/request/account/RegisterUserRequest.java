package com.tuanisreal.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.tuanisreal.constant.ParamKey;
import com.tuanisreal.controller.request.Request;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class RegisterUserRequest extends Request {
    @JsonProperty(ParamKey.USERNAME)
    private String username;

    @JsonProperty(ParamKey.GENDER)
    private Integer gender;

    @JsonProperty(ParamKey.DATE_OF_BIRTH)
    private String dateOfBirth;

    @JsonProperty(ParamKey.ADDRESS)
    private String address;

}
