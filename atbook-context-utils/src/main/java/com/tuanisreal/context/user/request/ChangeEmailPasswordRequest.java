package com.tuanisreal.context.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEmailPasswordRequest {
    private String userId;

    private String email;

    private String oldPassword;

    private String newPassword;
}
