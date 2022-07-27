package com.save_backend.src.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.abuse.model.GetAbuseRes;
import com.save_backend.src.abuse.model.PostAbuseSuspectReq;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;

@Service
public class AbuseProvider {
    private final AbuseDao abuseDao;

    public AbuseProvider(AbuseDao abuseDao) {
        this.abuseDao = abuseDao;
    }

    /**
     * 2. 학대 정황 조회
     */
    public GetAbuseRes getAbuseByIdx(int abuseIdx) throws BaseException{
        //존재하는 학대 정황(active)에 대한 조회인지 확인
        if(checkAbuse(abuseIdx)==0){
            throw new BaseException(NOT_EXIST_ABUSE_SITUATION);
        }
        try {
            GetAbuseRes getAbuseRes = abuseDao.getAbuseByIdx(abuseIdx);
            return getAbuseRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * Validation
     */
    public int checkChild(int childIdx) throws BaseException {
        try {
            return abuseDao.checkChild(childIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkSuspect(int suspectIdx) throws BaseException {
        try{
            return abuseDao.checkSuspect(suspectIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkAbuse(int abuseIdx) throws BaseException {
        try{
            return abuseDao.checkAbuse(abuseIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
