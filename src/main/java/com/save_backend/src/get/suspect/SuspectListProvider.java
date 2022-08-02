package com.save_backend.src.get.suspect;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.get.suspect.model.GetSuspectListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuspectListProvider {

    private final SuspectListDao suspectListDao;

    public List<GetSuspectListRes> getSuspectList(int childIdx) throws BaseException {
        if(!suspectListDao.isExistChild(childIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_CHILD);
        }

        try{
            return suspectListDao.getSuspectList(childIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
    }
}
