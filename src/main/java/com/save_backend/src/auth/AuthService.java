package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.auth.model.PatchAlarmRes;
import com.save_backend.src.child.ChildDao;
import com.save_backend.src.child.ChildProvider;
import com.save_backend.src.child.model.PostChildRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.save_backend.config.response.BaseResponseStatus.NOT_EXIST_USER;

@Service
public class AuthService {

    private final AuthDao authDao;
    private final AuthProvider authProvider;

    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider) {
        this.authDao = authDao;
        this.authProvider = authProvider;
    }

    public PatchAlarmRes changeAlarm(int userIdx) throws BaseException {
        //존재하는 유저(active)인지
        if(authProvider.checkUser(userIdx)==0){
            throw new BaseException(NOT_EXIST_USER);
        }
        try{
            return authDao.changeAlarm(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
