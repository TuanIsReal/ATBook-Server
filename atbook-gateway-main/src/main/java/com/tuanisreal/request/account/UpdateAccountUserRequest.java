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
public class UpdateAccountUserRequest extends Request {
    @JsonProperty(ParamKey.EMAIL)
    private String email;

    @JsonProperty(ParamKey.PASSWORD)
    private String newPassword;
}
