package com.save_backend.src.user;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;

@Service
public class UserService {

    private final UserDao userDao;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public PostUserRes generalSignUp(PostUserReq postUserReq) throws BaseException{
        //중복 여부 검사
        if (isExistEmail(postUserReq.getEmail())) {
            throw new BaseException(USERS_EXISTS_EMAIL);
        }
        if (isExistPhone(postUserReq.getPhone())) {
            throw new BaseException(USERS_EXISTS_PHONE_NUMBER);
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

    // 회원가입 시 이용
    private boolean isExistPhone(String phone) throws BaseException{
        try{
            return userDao.isExistPhone(phone);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원가입 시 이용
    private boolean isExistEmail(String email) throws BaseException{
        try{
            return userDao.isExistEmail(email);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PutUserInfoRes modifyUserInfo(int userIdx, PutUserInfoReq putUserInfoReq) throws BaseException {
        //회원 탈퇴 여부 검사
        if (!isValidUser(userIdx)){
            throw new BaseException(USERS_INACTIVE_USER_ID);
        }

        //중복 여부 검사
        if (isExistEmail(userIdx, putUserInfoReq.getEmail())) {
            throw new BaseException(USERS_EXISTS_EMAIL);
        }
        if (isExistPhone(userIdx, putUserInfoReq.getPhone())) {
            throw new BaseException(USERS_EXISTS_PHONE_NUMBER);
        }

        try{
            return userDao.modifyUserInfo(userIdx, putUserInfoReq);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private boolean isValidUser(int userIdx) throws BaseException {
        try{
            return userDao.isActiveUser(userIdx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원정보 수정 시 이용
    private boolean isExistPhone(int userIdx, String phone) throws BaseException{
        try{
            return userDao.isExistPhone(userIdx, phone);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원정보 수정 시 이용
    private boolean isExistEmail(int userIdx, String email) throws BaseException{
        try{
            return userDao.isExistEmail(userIdx, email);
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
}
