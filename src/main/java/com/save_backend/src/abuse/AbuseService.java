package com.save_backend.src.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.abuse.model.PatchAbuseDelRes;
import com.save_backend.src.abuse.model.PatchAbuseReq;
import com.save_backend.src.abuse.model.PostAbuseReq;
import com.save_backend.src.abuse.model.PostAbuseRes;
import com.save_backend.src.child.model.PatchChildDelRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.save_backend.config.response.BaseResponseStatus.*;

@Service
public class AbuseService {
    private final AbuseProvider abuseProvider;
    private final AbuseDao abuseDao;

    public AbuseService(AbuseProvider abuseProvider, AbuseDao abuseDao) {
        this.abuseProvider = abuseProvider;
        this.abuseDao = abuseDao;
    }

    /**
     * 1. 학대 정황 생성
     */
    public PostAbuseRes createAbuse(PostAbuseReq postAbuseReq) throws BaseException {
        //존재하는 아동에 대한 정황 생성인지 확인
        if(abuseProvider.checkChild(postAbuseReq.getChildIdx())==0){
            throw new BaseException(NOT_EXIST_CHILD);
        }
        try{
            //학대 유형 list -> string 변환
            List<String> type = postAbuseReq.getType();
            StringBuilder sb = new StringBuilder();
            for (String s : type) {
                sb.append(s);
                sb.append(",");
            }
            String typeStr = sb.toString();

            int abuseIdx = abuseDao.insertAbuse(typeStr, postAbuseReq);

            //학대정황-의심자 테이블에 추가
            for(int i = 0; i < postAbuseReq.getSuspect().size(); i++) {
                abuseDao.insertAbuseSuspect(abuseIdx, postAbuseReq.getSuspect().get(i));
            }
            return new PostAbuseRes(abuseIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 3. 학대 정황 수정
     */
    public void modifyAbuse(int abuseIdx, PatchAbuseReq patchAbuseReq) throws BaseException {
        //존재하는 정황(active)에 대한 modify인지 확인
        if(abuseProvider.checkAbuse(abuseIdx) ==0) {
            throw new BaseException(NOT_EXIST_ABUSE_SITUATION);
        }

        //의심자가 존재(active)하는지 확인
        for(int i = 0; i < patchAbuseReq.getSuspect().size(); i++) {
            if(abuseProvider.checkSuspect(patchAbuseReq.getSuspect().get(i))==0){
                throw new BaseException(NOT_EXIST_SUSPECT);
            }
        }

        try{
            int editResult = abuseDao.modifyAbuse(patchAbuseReq, abuseIdx);
            if(editResult == 0){
                throw new BaseException(MODIFY_FAIL_POST);
            }

            //학대-의심자 테이블에서 abuseIdx 일치하는 값 삭제 후 수정 진행
            abuseDao.deleteAbuseSuspect(abuseIdx);

            //학대정황-의심자 테이블 수정(추가)
            for(int i = 0; i < patchAbuseReq.getSuspect().size(); i++) {
                abuseDao.modifyAbuseSuspect(abuseIdx, patchAbuseReq.getSuspect().get(i));
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 4. 학대 정황 삭제 (active -> inactive)
     */
    public PatchAbuseDelRes deleteAbuse(int abuseIdx) throws BaseException {
        //존재하는 정황(active)에 대한 delete인지 확인
        if(abuseProvider.checkAbuse(abuseIdx)==0){
            throw new BaseException(NOT_EXIST_ABUSE_SITUATION);
        }
        try{
            return abuseDao.deleteAbuse(abuseIdx);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
