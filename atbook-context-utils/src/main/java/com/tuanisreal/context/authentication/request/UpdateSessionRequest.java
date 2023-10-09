package com.tuanisreal.context.authentication.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSessionRequest {
    String userId;
    String usingApplication;
    boolean finishRegisterUser;
    String refreshToken;
}
