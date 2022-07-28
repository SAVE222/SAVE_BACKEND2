package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.auth.model.PostLoginReq;
import com.save_backend.src.auth.model.PostLoginRes;
import com.save_backend.src.auth.model.User;
import com.save_backend.src.utils.jwt.JwtProperties;
import com.save_backend.src.utils.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.save_backend.config.response.BaseResponseStatus.FAILED_TO_LOGIN;
import static com.save_backend.config.response.BaseResponseStatus.INVALID_ACCESS_PASSWORD;


@Service
public class AuthService{

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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


}
