package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.auth.model.GetAlarmRes;
import com.save_backend.src.auth.model.PatchAlarmRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthProvider authProvider;
    private final AuthService authService;


    public AuthController(AuthProvider authProvider, AuthService authService){
        this.authProvider = authProvider;
        this.authService = authService;
    }

    /**
     * 알람 여부 조회 API
     */
    @ResponseBody
    @GetMapping("/alarm/{userIdx}")
    public BaseResponse<GetAlarmRes> getAlarm(@PathVariable("userIdx") int userIdx) {
        try{
            GetAlarmRes getAlarmRes = authProvider.getAlarm(userIdx);
            return new BaseResponse<>(getAlarmRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    /**
     * 알람 여부 변경 API
     */
    @ResponseBody
    @PatchMapping("/alarm/{userIdx}")
    public BaseResponse<PatchAlarmRes> changeAlarm(@PathVariable("userIdx") int userIdx) {
        try{
            PatchAlarmRes patchAlarmRes = authService.changeAlarm(userIdx);
            return new BaseResponse<>(patchAlarmRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
