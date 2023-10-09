package com.tuanisreal.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tuanisreal.constant.ParamKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse {
    @JsonProperty(ParamKey.TOKEN)
    private String token;

    @JsonProperty(ParamKey.REFRESH_TOKEN)
    private String refreshToken;
}
