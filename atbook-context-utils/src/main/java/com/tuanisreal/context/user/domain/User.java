package com.tuanisreal.context.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private Integer gender;
    private String dateOfBirth;
    private String address;
    private Long lastLogin;
    private Long registerTime;
    private Boolean isFinishRegister;
    private Long lastUpdate;
}
