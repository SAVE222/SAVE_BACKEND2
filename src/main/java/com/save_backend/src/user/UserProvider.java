package com.save_backend.src.user;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.user.model.GetUserInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.save_backend.config.response.BaseResponseStatus.USERS_INACTIVE_USER_ID;

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
            throw new BaseException(USERS_INACTIVE_USER_ID);
        }

        try{
            GetUserInfoRes result = userDao.getUserInfo(userIdx);
            return new BaseResponse<>(result);
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
}
