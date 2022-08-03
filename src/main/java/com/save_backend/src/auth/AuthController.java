package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.auth.model.*;
import com.save_backend.src.utils.jwt.JwtService;
import com.save_backend.src.utils.mail.MailDTO;
import com.save_backend.src.utils.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;

    @Autowired
    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService, RedisTemplate<String, String> redisTemplate, MailService mailService) {
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
        this.mailService = mailService;
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

            // jwt 추출
            String jwtToken = jwtService.getJwt();
            // jwt 토큰 검증
            if(!jwtService.validateToken(jwtToken)){
                return new BaseResponse<>(INVALID_JWT);
            }

            int userIdx = authService.tokenLogin(jwtToken);
            // 자동로그인 성공 true 반환
            String successMessage = "자동로그인에 성공했습니다.";
            return new BaseResponse<>(new GetUserRes(userIdx,successMessage));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 로그아웃
     */
    @ResponseBody
    @GetMapping ("/logout/{userIdx}")
    public BaseResponse<String> logout(@PathVariable("userIdx") int userIdx){
        try {
            // jwt 추출
            String jwtToken = jwtService.getJwt();
            // jwt 토큰 검증
            if(!jwtService.validateToken(jwtToken)){
                return new BaseResponse<>(INVALID_JWT);
            }
            // jwt에서 userIdx를 추출해 PathVariable로 받은 userIdx와 일치하는지 확인
            if(jwtService.getUserIdx() != userIdx) {
                throw new BaseException(INVALID_ACCESS_USER_JWT);
            }

            authService.logout(userIdx, jwtToken);
            String logoutMessage = "로그아웃을 성공했습니다";
            return new BaseResponse<>(logoutMessage);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 비밀번호 변경
     */
    @ResponseBody
    @PatchMapping ("/password/{userIdx}")
    public BaseResponse<PatchAuthRes> modifyPassword(@PathVariable("userIdx") int userIdx, @RequestBody PatchAuthReq patchAuthReq){
        try {
            // jwt 추출
            String jwtToken = jwtService.getJwt();
            // jwt 토큰 검증
            if(!jwtService.validateToken(jwtToken)){
                return new BaseResponse<>(INVALID_JWT);
            }
            // jwt에서 userIdx를 추출해 PathVariable로 받은 userIdx와 일치하는지 확인
            if(jwtService.getUserIdx() != userIdx) {
                throw new BaseException(INVALID_ACCESS_USER_JWT);
            }

            // 비밀번호 형식 확인
            if(!isValidPassword(patchAuthReq.getNewPassword())){
                return new BaseResponse<>(INVALID_PASSWORD);
            }
            String newPassword = authService.modifyPassword(userIdx,patchAuthReq);
            String successMessage = "비밀번호 변경을 성공했습니다";
            PatchAuthRes patchAuthRes = new PatchAuthRes(userIdx, newPassword, successMessage);
            return new BaseResponse<>(patchAuthRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 비밀번호 재생성 by email
     * 이메일로 임시 비밀번호 발송
     */
    @ResponseBody
    @GetMapping("/password")
    public BaseResponse<GetTempPasswordRes> recreatePassword(@RequestParam String email){
        try {
            // 이메일 형식 확인
            if(!isValidEmail(email)) {
                throw new BaseException(INVALID_EMAIL);
            }

            // 임시 비밀번호 생성
            String tempPassword = mailService.getTempPassword();
            // 유저 비밀번호를 임시 비밀번호로 변경
            authService.recreatePassword(email, tempPassword);
            // 임시 비밀번호를 담은 이메일 전송
            MailDTO mailDTO = mailService.createMail(email, tempPassword);
            mailService.mailSend(mailDTO, email);

            String sendMessage = "입력하신 이메일로 임시 비밀번호가 전송되었습니다.";
            GetTempPasswordRes getTempPasswordRes = new GetTempPasswordRes(tempPassword, sendMessage);
            return new BaseResponse<>(getTempPasswordRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
