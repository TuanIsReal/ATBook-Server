package com.tuanisreal.context.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNewTokenByRefreshTokenRequest {
    private String refreshToken;
}
