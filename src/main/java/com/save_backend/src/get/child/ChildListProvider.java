package com.save_backend.src.get.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.get.child.model.GetChildListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildListProvider {

    private final ChildListDao childListDao;

    public List<GetChildListRes> getChildList(int userIdx) throws BaseException {
        if(!childListDao.isUserExist(userIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_USER);
        }

        try{
            return childListDao.getChildList(userIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
    }
}
