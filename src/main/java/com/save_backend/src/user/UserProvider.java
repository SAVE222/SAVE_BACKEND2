package com.save_backend.src.user;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.user.model.GetAlarmRes;
import com.save_backend.src.user.model.GetUserInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;


@Service
public class UserProvider {

    private final UserDao userDao;

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }


    public BaseResponse<GetUserInfoRes> getUserInfo(int userIdx) throws BaseException {
        //탈퇴 처리 된 회원의 정보를 조회할 경우
        if(!isValidUser(userIdx)){
            throw new BaseException(NOT_EXIST_USER);
        }

        try{
            GetUserInfoRes result = userDao.getUserInfo(userIdx);
            return new BaseResponse<>(result);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetAlarmRes getAlarm(int userIdx) throws BaseException {
        if(!this.isValidUser(userIdx)){
            throw new BaseException(NOT_EXIST_USER);
        }
        try{
            return userDao.getAlarm(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * validation
     */

    // 유효한 유저인지 검증
    public boolean isValidUser(int userIdx) throws BaseException {
        try{
            return userDao.isActiveUser(userIdx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 존재하는 핸드폰 번호인지 검증
    // 1) 회원가입 시 이용
    public boolean isExistPhone(String phone) throws BaseException{
        try{
            return userDao.isExistPhone(phone);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 존재하는 핸드폰 번호인지 검증
    // 2) 회원정보 수정 시 이용
    public boolean isExistPhone(int userIdx, String phone) throws BaseException{
        try{
            return userDao.isExistPhone(userIdx, phone);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 존재하는 이메일인지 검증
    // 1) 회원가입 시 이용
    public boolean isExistEmail(String email) throws BaseException{
        try{
            return userDao.isExistEmail(email);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 존재하는 이메일인지 검증
    // 2) 회원정보 수정 시 이용
    public boolean isExistEmail(int userIdx, String email) throws BaseException{
        try{
            return userDao.isExistEmail(userIdx, email);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
