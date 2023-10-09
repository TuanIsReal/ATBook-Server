package com.tuanisreal.context.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tuanisreal.constant.Constant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckAuthenticationResponse {
    private String userId;
    private Integer sessionType;
    private String usingApplication;
    private Boolean isFinishRegisterUser;
    private Long timeExpiredToken;

    public boolean isSessionUser() {
        return this.sessionType == Constant.SESSION_TYPE.APPLICATION_USER;
    }
}
