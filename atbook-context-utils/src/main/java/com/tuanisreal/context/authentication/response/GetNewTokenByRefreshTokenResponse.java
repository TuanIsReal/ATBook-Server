package com.tuanisreal.context.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNewTokenByRefreshTokenResponse {
    private String newToken;
}
