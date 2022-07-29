package com.save_backend.src.utils.jwt;

import static com.save_backend.config.secret.Secret.JWT_SECRET_KEY;

public interface JwtProperties {
    String SECRET_KEY = JWT_SECRET_KEY;
    int EXPIRATION_TIME =  1*(1000*60*60*24*14); // 토큰의 만료기간 : 2주
    String TOKEN_PREFIX = "Bearer "; // 토큰 앞에 붙는 정해진 형식 (마지막에 공백필수)
    String HEADER_STRING = "X-ACCESS-TOKEN"; // JWT token을 넣을 응답헤더 항목

    String LOGOUT_FREFIX = "logoutToken: ";
}
