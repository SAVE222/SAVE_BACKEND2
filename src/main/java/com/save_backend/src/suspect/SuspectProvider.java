package com.save_backend.src.suspect;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SuspectProvider {

    private final SuspectDao suspectDao;

    public SuspectProvider(SuspectDao suspectDao) {
        this.suspectDao = suspectDao;
    }

    public int checkChild(int childIdx) throws BaseException {
        try{
            return suspectDao.checkChild(childIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
