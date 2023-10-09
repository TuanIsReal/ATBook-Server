package com.tuanisreal.context.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserRequest {
    private String email;
    private String password;
    private String application;
}
