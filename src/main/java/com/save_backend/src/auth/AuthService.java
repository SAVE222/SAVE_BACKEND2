package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.auth.model.PostLoginReq;
import com.save_backend.src.auth.model.PostLoginRes;
import com.save_backend.src.auth.model.User;
import com.save_backend.src.utils.jwt.JwtProperties;
import com.save_backend.src.utils.jwt.JwtService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.time.Duration;
import java.util.Date;

import static com.save_backend.config.response.BaseResponseStatus.*;
import static com.save_backend.src.utils.jwt.JwtProperties.LOGOUT_FREFIX;
import static com.save_backend.src.utils.jwt.JwtProperties.SECRET_KEY;


@Service
public class AuthService{

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authDao = authDao;
        this.authProvider = authProvider;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    public PostLoginRes login(PostLoginReq postLoginReq, HttpServletResponse response) throws BaseException {
        // 이메일을 통해 유저정보 가져오기
        try{
            User user = authDao.getUserByEmail(postLoginReq.getEmail());
            // TODO : 이메일 존재 여부 확인
            String userPwd = user.getPassword();
            if(passwordEncoder.matches(postLoginReq.getPassword(), userPwd)){
                // 동일하면 jwt 발급
                int userIdx = user.getUserIdx();
                String jwt = JwtProperties.TOKEN_PREFIX + jwtService.createJwt(userIdx);

                // 발급받은 jwt를 응답헤더에 추가
                response.setHeader(JwtProperties.HEADER_STRING, jwt);
                return new PostLoginRes(userIdx, jwt);
            } else {
                throw new BaseException(INVALID_ACCESS_PASSWORD);
            }
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public int tokenLogin(String jwtToken) throws BaseException {
        // Redis에 token 존재하는지 확인 -> 없으면 성공, 있으면 실패
        if(redisTemplate.opsForValue().get(LOGOUT_FREFIX + jwtToken) != null) {
            // 로그아웃처리된 token입니다
            throw new BaseException(AREADY_LOGOUT_USER);
        }

        // jwt 로 유저인덱스 가져오기
        int userIdx = jwtService.getUserIdx();
        // 해당 유저가 Active한지 확인
        if(authProvider.checkUser(userIdx) == 0){
            throw new BaseException(NOT_EXIST_USER);
        }
        return userIdx;
    }

    public void logout(int userIdx,String jwtToken) throws BaseException {
        // Redis에 token 존재하는지 확인 -> 없으면 성공, 있으면 실패
        if(redisTemplate.opsForValue().get(LOGOUT_FREFIX + jwtToken) != null) {
            throw new BaseException(AREADY_LOGOUT_USER);
        }
        // 토큰 남은시간 계산후 해당 시간만큼 만료시간을 정해 해당 토큰을 Redis에 저장
        Long expiration = jwtService.getExpiration(jwtToken);
        setDataExpired(LOGOUT_FREFIX +jwtToken, jwtToken, expiration);
    }

    public void setDataExpired(String key, Object value, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        redisTemplate.opsForValue().set(key, String.valueOf(value),expireDuration);
    }
}
