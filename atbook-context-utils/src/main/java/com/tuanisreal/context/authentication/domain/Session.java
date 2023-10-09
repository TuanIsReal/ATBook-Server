package com.tuanisreal.context.authentication.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.tuanisreal.constant.Constant;

@Data
@Builder
@AllArgsConstructor
public class Session {
    private String userId;
    private String refreshToken;
    @Builder.Default
    private int sessionType = Constant.SESSION_TYPE.APPLICATION_USER;
    private String usingApplication;
    private boolean finishRegisterUser;
    @Builder.Default
    private long sessionExpire = 0;
}
