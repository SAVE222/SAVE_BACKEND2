package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.auth.model.GetAlarmRes;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.save_backend.config.response.BaseResponseStatus.NOT_EXIST_USER;

@Service
public class AuthProvider {
    private final AuthDao authDao;

    public AuthProvider(AuthDao authDao) {
        this.authDao = authDao;
    }

    public GetAlarmRes getAlarm(int userIdx) throws BaseException {
        if(this.checkUser(userIdx)==0){
            throw new BaseException(NOT_EXIST_USER);
        }
        try{
            return authDao.getAlarm(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * validation
     */
    public int checkUser(int userIdx) throws BaseException {
        try{
            return authDao.checkUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
