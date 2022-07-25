package com.save_backend.src.auth;

import com.save_backend.config.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;

@Service
public class AuthProvider {
    private final AuthDao authDao;

    @Autowired
    public AuthProvider(AuthDao authDao) {
        this.authDao = authDao;
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
