package com.save_backend.src.user;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.user.model.PatchAlarmRes;
import com.save_backend.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;

@Service
public class UserService {

    private final UserProvider userProvider;
    private final UserDao userDao;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserProvider userProvider, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userProvider = userProvider;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public PostUserRes generalSignUp(PostUserReq postUserReq) throws BaseException{
        //중복 여부 검사
        if (userProvider.isExistEmail(postUserReq.getEmail())) {
            throw new BaseException(EXISTS_EMAIL);
        }
        if (userProvider.isExistPhone(postUserReq.getPhone())) {
            throw new BaseException(EXISTS_PHONE_NUMBER);
        }

        try{
            String pwd = passwordEncoder.encode(postUserReq.getPassword()); //비밀번호 암호화
            postUserReq.setPassword(pwd);
        }catch(Exception e){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            int result = userDao.generalSignUp(postUserReq);
            return new PostUserRes(result);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public PutUserInfoRes modifyUserInfo(int userIdx, PutUserInfoReq putUserInfoReq) throws BaseException {
        //회원 탈퇴 여부 검사
        if (!userProvider.isValidUser(userIdx)){
            throw new BaseException(NOT_EXIST_USER);
        }

        //중복 여부 검사
        if (userProvider.isExistEmail(userIdx, putUserInfoReq.getEmail())) {
            throw new BaseException(EXISTS_EMAIL);
        }
        if (userProvider.isExistPhone(userIdx, putUserInfoReq.getPhone())) {
            throw new BaseException(EXISTS_PHONE_NUMBER);
        }

        try{
            return userDao.modifyUserInfo(userIdx, putUserInfoReq);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserRes deleteUser(int user_idx) throws BaseException {
        try{
            return userDao.deleteUser(user_idx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchAlarmRes changeAlarm(int userIdx) throws BaseException {
        //존재하는 유저(active)인지
        if(!userProvider.isValidUser(userIdx)){
            throw new BaseException(NOT_EXIST_USER);
        }
        try{
            return userDao.changeAlarm(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
