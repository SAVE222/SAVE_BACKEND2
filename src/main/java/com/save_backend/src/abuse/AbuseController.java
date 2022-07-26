package com.save_backend.src.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.abuse.model.*;
import com.save_backend.src.child.ChildProvider;
import com.save_backend.src.child.ChildService;
import com.save_backend.src.child.model.PatchChildEditReq;
import com.save_backend.src.child.model.PatchChildEditRes;
import com.save_backend.src.child.model.PostChildReq;
import com.save_backend.src.child.model.PostChildRes;
import com.save_backend.src.suspect.model.GetSuspectRes;
import org.springframework.web.bind.annotation.*;

import static com.save_backend.config.response.BaseResponseStatus.*;
import static com.save_backend.config.response.BaseResponseStatus.EMPTY_CHILD_ADDRESS;

@RestController
@RequestMapping("/abuse")
public class AbuseController {
    private final AbuseProvider abuseProvider;
    private final AbuseService abuseService;

    public AbuseController(AbuseProvider abuseProvider, AbuseService abuseService) {
        this.abuseProvider = abuseProvider;
        this.abuseService = abuseService;
    }

    /**
     * 1. 학대 정황 생성 api
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostAbuseRes> createAbuse(@RequestBody PostAbuseReq postAbuseReq) {
        try {
            // 아동인덱스 필수
            if (postAbuseReq.getChildIdx() == 0) {
                return new BaseResponse<>(EMPTY_CHILD_INDEX);
            }
            // 날짜 선택 필수
            if(postAbuseReq.getDate() == null){
                return new BaseResponse<>(EMPTY_ABUSE_DATE);
            }
            // 시간 선택 필수
            if (postAbuseReq.getTime() == null) {
                return new BaseResponse<>(EMPTY_ABUSE_TIME);
            }
            // 장소 선택 필수
            if (postAbuseReq.getPlace() == null) {
                return new BaseResponse<>(EMPTY_ABUSE_PLACE);
            }
            // 구체적 의심정황 입력 필수
            if (postAbuseReq.getDetail() == null) {
                return new BaseResponse<>(EMPTY_ABUSE_DESCRIPTION);
            }
            //학대 의심자 선택 필수
            if (postAbuseReq.getSuspect().size() < 1) {
                return new BaseResponse<>(EMPTY_SUSPECT_INDEX);
            }
            // 학대유형 선택 필수
            if (postAbuseReq.getType().size() < 1) {
                return new BaseResponse<>(EMPTY_ABUSE_TYPE);
            }

            PostAbuseRes postAbuseRes = abuseService.createAbuse(postAbuseReq);
            return new BaseResponse<>(postAbuseRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 2. 학대 정황 조회 API
     */
    @ResponseBody
    @GetMapping("/{abuseIdx}")
    public BaseResponse<GetAbuseRes> getAbuseByIdx(@PathVariable("abuseIdx")int abuseIdx){
        try {
            GetAbuseRes getAbuseRes = abuseProvider.getAbuseByIdx(abuseIdx);
            return new BaseResponse<>(getAbuseRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 3. 학대 정황 수정 api
     */
    @ResponseBody
    @PatchMapping("/{abuseIdx}")
    public BaseResponse<String> modifyAbuse(@PathVariable("abuseIdx") int abuseIdx, @RequestBody PatchAbuseReq patchAbuseReq){
        // 날짜 선택 필수
        if(patchAbuseReq.getDate() == null){
            return new BaseResponse<>(EMPTY_ABUSE_DATE);
        }
        // 시간 선택 필수
        if (patchAbuseReq.getTime() == null) {
            return new BaseResponse<>(EMPTY_ABUSE_TIME);
        }
        // 장소 선택 필수
        if (patchAbuseReq.getPlace() == null) {
            return new BaseResponse<>(EMPTY_ABUSE_PLACE);
        }
        // 구체적 의심정황 입력 필수
        if (patchAbuseReq.getDetail() == null) {
            return new BaseResponse<>(EMPTY_ABUSE_DESCRIPTION);
        }
        //학대 의심자 선택 필수
//        if (patchAbuseReq.getSuspect().size() < 1) {
//            return new BaseResponse<>(EMPTY_SUSPECT_INDEX);
//        }
        // 학대유형 선택 필수
        if (patchAbuseReq.getType().size() < 1) {
            return new BaseResponse<>(EMPTY_ABUSE_TYPE);
        }
        try {
            abuseService.modifyAbuse(abuseIdx, patchAbuseReq);
            String editResult = "정황 수정을 완료하였습니다.";
            return new BaseResponse<>(editResult);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
