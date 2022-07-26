package com.save_backend.src.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.abuse.model.PostAbuseReq;
import com.save_backend.src.abuse.model.PostAbuseRes;
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
        //학대 의심자 존재하는지 확인
        if(abuseProvider.checkSuspect(postAbuseReq.getChildIdx())==0){
            throw new BaseException(NOT_EXIST_SUSPECT);
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


}