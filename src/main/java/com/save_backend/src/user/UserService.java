package com.save_backend.src.user;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
            System.out.println(pwd);
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

    private boolean isExistPhone(String phone) throws BaseException{
        try{
            if(userDao.isExistPhone(phone) == 1) {
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private boolean isExistEmail(String email) throws BaseException{
        try{
            if(userDao.isExistEmail(email) == 1) {
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PutUserInfoRes modifyUserInfo(PutUserInfoReq patchUserInfoReq) throws BaseException {
        //중복 여부 검사
        if (isExistEmail(patchUserInfoReq.getEmail())) {
            throw new BaseException(USERS_EXISTS_EMAIL);
        }
        if (isExistPhone(patchUserInfoReq.getPhone())) {
            throw new BaseException(USERS_EXISTS_PHONE_NUMBER);
        }

        try{
            return userDao.modifyUserInfo(patchUserInfoReq);
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
