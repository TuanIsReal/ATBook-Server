package com.tuanisreal.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuanisreal.controller.request.Request;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenRequest extends Request {

    @JsonProperty("refresh_token")
    private String refreshToken;
}
