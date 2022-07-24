package com.save_backend.src.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.child.ChildDao;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
@Service
public class AbuseProvider {
    private final AbuseDao abuseDao;

    public AbuseProvider(AbuseDao abuseDao) {
        this.abuseDao = abuseDao;
    }

    /**
     * Validation
     */
    public int checkChild(int childIdx) throws BaseException {
        try {
            return abuseDao.checkChild(childIdx);
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
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
}
