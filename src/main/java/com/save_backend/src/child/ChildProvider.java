package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.child.model.GetChildInfoRes;

import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ChildProvider {
    private final ChildDao childDao;

    public ChildProvider(ChildDao childDao) {
        this.childDao = childDao;
    }

    public GetChildInfoRes getChildInfoByIdx(int childIdx) throws BaseException {
        try {
            GetChildInfoRes getChildInfoRes = childDao.getChildInfoByIdx(childIdx);
            return getChildInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUser(int userIdx) throws BaseException {
        try{
            return childDao.checkUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkChild(int childIdx) throws BaseException {
        try{
            return childDao.checkChild(childIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
