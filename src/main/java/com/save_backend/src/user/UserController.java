package com.save_backend.src.user;


import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.user.model.GetAlarmRes;
import com.save_backend.src.user.model.PatchAlarmRes;
import com.save_backend.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.save_backend.src.utils.ValidationRegex.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserProvider userProvider;

    @Autowired
    public UserController(UserService userService, UserProvider userProvider) {
        this.userService = userService;
        this.userProvider = userProvider;
    }



    /**
     * 회원가입
     */
    @PostMapping("")
    public BaseResponse<PostUserRes> generalSignUp(@RequestBody PostUserReq postUserReq){
        //공란인 경우 처리
        if(postUserReq.getIsSnsAuth() == 1 || postUserReq.getName() == null || postUserReq.getPhone() == null || postUserReq.getEmail() == null || postUserReq.getPassword() == null){
            return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
        }

        if(!isValidPhone(postUserReq.getPhone())){
            return new BaseResponse<>(BaseResponseStatus.INVALID_PHONE_NUMBER);
        }
        if(!isValidEmail(postUserReq.getEmail())){
            return new BaseResponse<>(BaseResponseStatus.INVALID_EMAIL);
        }
        if(!isValidPassword(postUserReq.getPassword())){
            return new BaseResponse<>(BaseResponseStatus.INVALID_PASSWORD);
        }

        try{
            PostUserRes postUserRes = userService.generalSignUp(postUserReq);
            return new BaseResponse<>(postUserRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 회원정보 조회
     * (비밀번호 제외)
     */
    @GetMapping("/{userIdx}")
    public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable int userIdx){
        try{
            return userProvider.getUserInfo(userIdx);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 회원정보 수정
     * (조회 API를 먼저 호출하여 정보를 받아온 뒤 호출)
     */
    @PutMapping("/{userIdx}")
    public BaseResponse<PutUserInfoRes> modifyUserInfo(@PathVariable int userIdx, @RequestBody PutUserInfoReq putUserInfoReq){
        //공란인 경우 처리
        if(putUserInfoReq.getName() == null || putUserInfoReq.getPhone() == null || putUserInfoReq.getEmail() == null){
            return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
        }

        if(!isValidPhone(putUserInfoReq.getPhone())){
            return new BaseResponse<>(BaseResponseStatus.INVALID_PHONE_NUMBER);
        }
        if(!isValidEmail(putUserInfoReq.getEmail())){
            return new BaseResponse<>(BaseResponseStatus.INVALID_EMAIL);
        }

        try{
            PutUserInfoRes putUserInfoRes = userService.modifyUserInfo(userIdx, putUserInfoReq);
            return new BaseResponse<>(putUserInfoRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * 회원탈퇴
     */
    @PatchMapping("/status/{userIdx}")
    public BaseResponse<PatchUserRes> deleteUser(@PathVariable int userIdx){
        try{
            PatchUserRes patchUserRes = userService.deleteUser(userIdx);
            return new BaseResponse<>(patchUserRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 알람 여부 조회 API
     */
    @ResponseBody
    @GetMapping("/alarm/{userIdx}")
    public BaseResponse<GetAlarmRes> getAlarm(@PathVariable("userIdx") int userIdx) {
        try{
            GetAlarmRes getAlarmRes = userProvider.getAlarm(userIdx);
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
            PatchAlarmRes patchAlarmRes = userService.changeAlarm(userIdx);
            return new BaseResponse<>(patchAlarmRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
