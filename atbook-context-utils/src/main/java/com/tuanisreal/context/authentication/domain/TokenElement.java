package com.tuanisreal.context.authentication.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenElement {
    private String userId;
    private String usingApplication;
    private Integer sessionType;
    private boolean finishRegisterUser;

    public TokenElement(String usingApplication) {
        this.usingApplication = usingApplication;
    }
}
