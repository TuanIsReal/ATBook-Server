package com.tuanisreal.token;

import com.sun.org.apache.xml.internal.security.Init;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import com.tuanisreal.constant.Constant;
import com.tuanisreal.context.authentication.domain.TokenElement;
import com.tuanisreal.utils.DateTimeUtil;

import java.util.Objects;

@Slf4j
public class JWTCreator {
    private static final String SECRET_KEY = "tuanisreal_1Qaz2Wsx";
    private JWTCreator() {
        Init.init();
    }

    private static final JWTCreator instance = new JWTCreator();

    public static JWTCreator getInstance() {
        return instance;
    }

    public String generateJWT(TokenElement tokenElement) {
        Claims claims = Jwts.claims();
        Long time = DateTimeUtil.currentTime();
        put(claims, TokenConstant.USER_ID, tokenElement.getUserId());
        put(claims, TokenConstant.APP_USING, tokenElement.getUsingApplication());
        put(claims, TokenConstant.SESSION_TYPE, tokenElement.getSessionType());
        put(claims, TokenConstant.FINISH_REGISTER, tokenElement.isFinishRegisterUser());
        JwtBuilder builder = Jwts.builder();
        builder.setClaims(claims);
        builder.setIssuedAt(DateTimeUtil.getDateTime(time));
        builder.setExpiration(DateTimeUtil.getDateTime(time + Constant.AN_HOUR));
        return builder.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public TokenElement parseJWT(String token) {
        try {
            JwtParser parser = Jwts.parser();
            parser.setSigningKey(SECRET_KEY);
            Claims body = parser.parseClaimsJws(token).getBody();
            return parseTokenBody(body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TokenElement parseTokenBody(Claims body) {
        TokenElement tokenElement = new TokenElement();
        tokenElement.setUserId((String) body.get(TokenConstant.USER_ID));
        tokenElement.setUsingApplication((String) body.get(TokenConstant.APP_USING));
        tokenElement.setSessionType((Integer) body.get(TokenConstant.SESSION_TYPE));
        tokenElement.setFinishRegisterUser((Boolean) body.get(TokenConstant.FINISH_REGISTER));
        return tokenElement;
    }

    private void put(Claims claims, String key, Object value) {
        if (Objects.nonNull(value)) {
            claims.put(key, value);
        }
    }

}
