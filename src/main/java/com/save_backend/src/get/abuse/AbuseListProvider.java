package com.save_backend.src.get.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.get.abuse.model.GetAbuseListRes;
import com.save_backend.src.get.child.ChildListDao;
import com.save_backend.src.get.child.model.GetChildListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbuseListProvider {
    private final AbuseListDao abuseListDao;

    public List<GetAbuseListRes> getAbuseList(int childIdx) throws BaseException {
        if(!abuseListDao.isChildExist(childIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_CHILD);
        }

        try{
            return abuseListDao.getAbuseList(childIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
    }
}
