package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.auth.model.GetUserRes;
import com.save_backend.src.auth.model.PostLoginReq;
import com.save_backend.src.auth.model.PostLoginRes;
import com.save_backend.src.utils.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.save_backend.config.response.BaseResponseStatus.*;
import static com.save_backend.src.utils.ValidationRegex.isValidEmail;
import static com.save_backend.src.utils.ValidationRegex.isValidPassword;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthProvider authProvider;
    private final AuthService authService;
    private final JwtService jwtService;


    @Autowired
    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService){
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 API
     * 입력받은 아이디와 비밀번호가 일치하면 jwt 토큰 발급
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq, HttpServletResponse response){
        try {
            // 이메일, 비밀번호 입력 확인
            if(postLoginReq.getEmail() == null){
                return new BaseResponse<>(EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null){
                return new BaseResponse<>(EMPTY_PASSWORD);
            }
            // 이메일 형식 확인
            if(!isValidEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(INVALID_EMAIL);
            }
            // 비밀번호 형식 확인
            if(!isValidPassword(postLoginReq.getPassword())){
                return new BaseResponse<>(INVALID_PASSWORD);
            }
            PostLoginRes postLoginRes = authService.login(postLoginReq, response);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 자동로그인
     * 헤더 - X-ACCESS-TOKEN : jwt token
     */
    @ResponseBody
    @GetMapping("/token")
    public BaseResponse<GetUserRes> tokenLogin(){
        try {
            String jwtToken = jwtService.getJwt();
            // jwt 없으면 fail
            if(jwtToken==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            // jwt 유효성검증(만료시간)
            if(!jwtService.validateToken(jwtToken)){
                return new BaseResponse<>(INVALID_JWT);
            }
            // jwt 로 유저인덱스 가져오기
            int userIdx = jwtService.getUserIdx();
            // 해당 유저가 Active한지 확인
            if(authProvider.checkUser(userIdx) == 0){
                return new BaseResponse<>(NOT_EXIST_USER);
            }
            // Active 하다면 자동로그인 성공 true 반환
            String successMessage = "자동로그인에 성공했습니다.";
            return new BaseResponse<>(new GetUserRes(userIdx,successMessage));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
