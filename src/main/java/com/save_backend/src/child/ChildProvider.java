package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.child.model.GetChildInfoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
@Service
public class ChildProvider {
    private final ChildDao childDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ChildProvider(ChildDao childDao) {
        this.childDao = childDao;
    }

    public GetChildInfoRes getChildInfoByIdx(int childIdx) throws BaseException {
        try{
            GetChildInfoRes getChildInfoRes = childDao.getChildInfoByIdx(childIdx);
            return getChildInfoRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
