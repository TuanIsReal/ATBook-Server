package com.tuanisreal.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class Config {
    @Value("${application.session.timeout}")
    private long sessionTimeout;

    @Value("${application.refreshToken.timeout}")
    private long refreshTokenTimeout;
}
