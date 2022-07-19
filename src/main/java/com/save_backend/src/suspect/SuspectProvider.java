package com.save_backend.src.suspect;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.suspect.model.GetSuspectRes;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.save_backend.config.response.BaseResponseStatus.NOT_EXIST_SUSPECT;

@Service
public class SuspectProvider {

    private final SuspectDao suspectDao;
    public SuspectProvider(SuspectDao suspectDao) {
        this.suspectDao = suspectDao;
    }

    /**
     * 2. 학대의심자 정보 조회
     */
    public GetSuspectRes getSuspectByIdx(int suspectIdx) throws BaseException{
        // 해당 학대의심자가 존재하는지 확인하기(ACTIVE 상태인지 확인)
        if(checkSuspect(suspectIdx)==0){
            throw new BaseException(NOT_EXIST_SUSPECT);
        }
        try {
            GetSuspectRes getSuspectRes = suspectDao.getSuspectByIdx(suspectIdx);
            return getSuspectRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * Validation
     */
    public int checkChild(int childIdx) throws BaseException {
        try{
            return suspectDao.checkChild(childIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkSuspect(int suspectIdx) throws BaseException {
        try{
            return suspectDao.checkSuspect(suspectIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
