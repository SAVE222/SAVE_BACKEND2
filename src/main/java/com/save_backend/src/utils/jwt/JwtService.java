package com.save_backend.src.utils.jwt;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import io.jsonwebtoken.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.save_backend.config.response.BaseResponseStatus.*;
import static com.save_backend.src.utils.jwt.JwtProperties.EXPIRATION_TIME;
import static com.save_backend.src.utils.jwt.JwtProperties.SECRET_KEY;

@Service
public class JwtService {
    /*
        1. jwt 토큰 생성
     */
    public String createJwt(int userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")  // header
                .claim("userIdx",userIdx)   // payload
                .setIssuedAt(now)  // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // signature (알고리즘, 시크릿키)
                .compact();
    }

    /*
        2. jwt 토큰 추출
        Request Header - "X-ACCESS-TOKEN" : "jwt token"
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("X-ACCESS-TOKEN");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    /*
        3. jwt를 이용해 userIdx 추출
     */
    public int getUserIdx() throws BaseException {
        //1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx", Integer.class);
    }

    /*
        4. Jwt 토큰 유효성 검사
     */
    public boolean validateToken(String jwtToken) throws Exception {
        // jwt token 유무
        if(jwtToken==null){
            throw new BaseException(EMPTY_JWT);
        }

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException ex) {
            // jwt 서명 유효성
            throw new BaseException(INVALID_JWT_SIGNATURE);
        } catch (MalformedJwtException ex) {
            // jwt 형식
            throw new BaseException(INVALID_JWT);
        } catch (ExpiredJwtException ex) {
            // jwt 만료시간
            throw new BaseException(INVALID_JWT_EXPIRED_TIME);
        } catch (UnsupportedJwtException ex) {
            // jwt 지원형식
            throw new BaseException(INVALID_UNSUPPORTED_JWT);
        } catch (IllegalArgumentException ex) {
            // jwt claims 유무
            throw new BaseException(EMPTY_JWT_CLAIMS);
        }
    }

    /*
        5. Token 남은 유효시간 계산
     */
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}

