package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.auth.model.*;
import com.save_backend.src.utils.jwt.JwtProperties;
import com.save_backend.src.utils.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;

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
        User user = authDao.getUserByEmail(postLoginReq.getEmail());
        // 이메일 존재 여부 확인
        if(authProvider.checkEmail(user.getEmail()) == 0){
            throw new BaseException(NOT_EXIST_EMAIL);
        }

        // 비밀번호 일치 여부 확인
        String userPwd = user.getPassword();
        String jwt;
        if(passwordEncoder.matches(postLoginReq.getPassword(), userPwd)){
            // 동일하면 jwt 발급
            jwt = JwtProperties.TOKEN_PREFIX + jwtService.createJwt(user.getUserIdx());
        } else {
            throw new BaseException(INVALID_ACCESS_PASSWORD);
        }

        // 발급받은 jwt를 응답헤더에 추가
        try{
            response.setHeader(JwtProperties.HEADER_STRING, jwt);
            return new PostLoginRes(user.getUserIdx(), jwt);
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

    public void logout(int userIdx, String jwtToken) throws BaseException {
        // jwt에서 userIdx를 추출해 PathVariable로 받은 userIdx와 일치하는지 확인
        if(jwtService.getUserIdx() != userIdx) {
            throw new BaseException(INVALID_ACCESS_USER_JWT);
        }
        // Redis에 token 존재하는지 확인 -> 없으면 성공, 있으면 실패
        if(redisTemplate.opsForValue().get(LOGOUT_FREFIX + jwtToken) != null) {
            throw new BaseException(AREADY_LOGOUT_USER);
        }
        // 토큰 남은시간 계산후 해당 시간만큼 만료시간을 정해 해당 토큰을 Redis에 저장
        try {
            Long expiration = jwtService.getExpiration(jwtToken);
            setDataExpired(LOGOUT_FREFIX +jwtToken, jwtToken, expiration);
        } catch (Exception exception) {
            throw new BaseException(REDIS_ERROR);
        }
    }

    public void setDataExpired(String key, Object value, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        redisTemplate.opsForValue().set(key, String.valueOf(value),expireDuration);
    }

    public String modifyPassword(int userIdx, PatchAuthReq patchAuthReq) throws BaseException  {
        // 유저 존재여부
        if(authProvider.checkUser(userIdx) == 0){
            throw new BaseException(NOT_EXIST_USER);
        }
        // 기존 비밀번호와 입력된 비밀번호가 다른지
        if(patchAuthReq.getNewPassword().equals(patchAuthReq.getOriginPassword())) {
            throw new BaseException(EXISTS_PASSWORD);
        }

        // 유저의 기존 비밀번호 가져오기
        String userPassword;
        try {
            userPassword = authDao.getPassword(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        // 입력된 기존 비밀번호가 해당 유저의 비밀번호와 일치하는지
        if(!passwordEncoder.matches(patchAuthReq.getOriginPassword(), userPassword)){
            throw new BaseException(INVALID_ACCESS_PASSWORD);
        }

        // 새로 입력받은 비밀번호 암호화
        String encryptedNewPassword;
        try{
            encryptedNewPassword = passwordEncoder.encode(patchAuthReq.getNewPassword());
        } catch(Exception e){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        // DB의 유저 비밀번호 변경
        try {
            int result = authDao.modifyPassword(userIdx, encryptedNewPassword);
            if(result == 0) {
                throw new BaseException(MODIFY_FAIL_PASSWORD);
            }
            return patchAuthReq.getNewPassword();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void recreatePassword(String email, String tempPassword) throws BaseException {
        // 이메일 존재여부 확인
        if(authProvider.checkEmail(email) == 0){
            throw new BaseException(NOT_EXIST_EMAIL);
        }

        // 임시 비밀번호 암호화
        String encryptedTempPassword;
        try{
            encryptedTempPassword = passwordEncoder.encode(tempPassword);
        } catch(Exception e){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        // DB에서 유저 비밀번호를 암호화한 임시비밀번호로 변경
        try {
            authDao.recreatePassword(email, encryptedTempPassword);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
