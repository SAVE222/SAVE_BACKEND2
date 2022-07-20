package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.child.model.GetChildInfoRes;

import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;

@Service
public class ChildProvider {
    private final ChildDao childDao;

    public ChildProvider(ChildDao childDao) {
        this.childDao = childDao;
    }


    /**
     * 2. 아동 정보 조회
     */
    public GetChildInfoRes getChildInfoByIdx(int childIdx) throws BaseException {
        //존재하는 아동(active)에 대한 조회인지 확인
        if(checkChild(childIdx)==0){
            throw new BaseException(NOT_EXIST_CHILD);
        }
        try {
            GetChildInfoRes getChildInfoRes = childDao.getChildInfoByIdx(childIdx);
            return getChildInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * Validation
     */
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
