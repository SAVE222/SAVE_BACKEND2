package com.save_backend.src.suspect;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.suspect.model.PatchSuspectReq;
import com.save_backend.src.suspect.model.PatchSuspectRes;
import com.save_backend.src.suspect.model.PostSuspectReq;
import com.save_backend.src.suspect.model.PostSuspectRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;

@Service
public class SuspectService {
    private final SuspectProvider suspectProvider;
    private final SuspectDao suspectDao;

    @Autowired
    public SuspectService(SuspectProvider suspectProvider, SuspectDao suspectDao) {
        this.suspectProvider = suspectProvider;
        this.suspectDao = suspectDao;
    }

    /**
     * 1. 학대의심자 정보 생성
     */
    public PostSuspectRes createSuspect(PostSuspectReq postSuspectReq) throws BaseException {
        // 아동 존재여부(+ACTIVE 상태인지 확인하기)
        if(suspectProvider.checkChild(postSuspectReq.getChildIdx())==0){
            throw new BaseException(NOT_EXIST_CHILD);
        }

        try{
            int suspectIdx = suspectDao.createSuspect(postSuspectReq);
            return new PostSuspectRes(suspectIdx);
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 3. 학대의심자 정보 수정
     */
    public PatchSuspectRes modifyCertainSuspect(int suspectIdx, PatchSuspectReq patchSuspectReq) throws BaseException {
        // 학대의심자 존재여부(+ACTIVE 상태인지 확인)
        if(suspectProvider.checkSuspect(suspectIdx)==0){
            throw new BaseException(NOT_EXIST_SUSPECT);
        }

        try{
            PatchSuspectRes patchSuspectRes = suspectDao.modifyCertainSuspect(suspectIdx, patchSuspectReq);
            return patchSuspectRes;
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 4. 학대의심자 정보 삭제
     */
    public void deleteCertainSuspect(int suspectIdx) throws BaseException {
        // 학대의심자 존재여부
        if(suspectProvider.checkSuspect(suspectIdx)==0){
            throw new BaseException(NOT_EXIST_SUSPECT);
        }

        try{
            int result = suspectDao.deleteCertainSuspect(suspectIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_SUSPECT);
            }
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
