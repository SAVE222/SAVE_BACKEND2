package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.child.model.*;
import org.springframework.web.bind.annotation.*;

import static com.save_backend.config.response.BaseResponseStatus.*;

@RestController
@RequestMapping("/child")
public class ChildController {

    private final ChildProvider childProvider;
    private final ChildService childService;


    public ChildController(ChildProvider childProvider, ChildService childService){
        this.childProvider = childProvider;
        this.childService = childService;
    }

    /**
     * 1. 아동 정보 생성 api
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostChildRes> createChild(@RequestBody PostChildReq postChildReq) {

        try {
            // 유저인덱스 필수
            if (postChildReq.getUserIdx() == 0) {
                return new BaseResponse<>(EMPTY_USER_INDEX);
            }
            // 아동 이름 필수
            if (postChildReq.getName() == null) {
                return new BaseResponse<>(EMPTY_CHILD_NAME);
            }
            // 아동 성별 필수
            String gender = postChildReq.getGender();
            if (gender == null) {
                return new BaseResponse<>(EMPTY_CHILD_GENDER);
            }
            // 성별은 female, male, unknown 중 하나
            if(!gender.equals("female") && !gender.equals("male") && !gender.equals("unknown")){
                return new BaseResponse<>(INVALID_GENDER);
            }
            // 아동 나이 필수
            if (postChildReq.getAge() == null) {
                return new BaseResponse<>(EMPTY_CHILD_AGE);
            }
            // 아동 주소 필수
            if (postChildReq.getAddress() == null) {
                return new BaseResponse<>(EMPTY_CHILD_ADDRESS);
            }

            PostChildRes postChildRes = childService.createChild(postChildReq);
            return new BaseResponse<>(postChildRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 2. 아동 정보 조회 api
     */
    @ResponseBody
    @GetMapping("/{childIdx}")
    public BaseResponse<GetChildInfoRes> getChildInfoByIdx(@PathVariable("childIdx") int childIdx) {
        try{

            GetChildInfoRes getChildInfoRes = childProvider.getChildInfoByIdx(childIdx);
            return new BaseResponse<>(getChildInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 3. 아동 정보 수정 api
     */
    @ResponseBody
    @PatchMapping("/{childIdx}")
    public BaseResponse<PatchChildEditRes> modifyChild(@PathVariable("childIdx") int childIdx, @RequestBody PatchChildEditReq patchChildEditReq){
        // 이름 필수
        if (patchChildEditReq.getName() == null) {
            return new BaseResponse<>(EMPTY_CHILD_NAME);
        }
        // 성별 필수
        String gender = patchChildEditReq.getGender();
        if (gender == null) {
            return new BaseResponse<>(EMPTY_CHILD_GENDER);
        }
        // 성별은 female, male, unknown 중 하나
        if(!gender.equals("female") && !gender.equals("male") && !gender.equals("unknown")){
            return new BaseResponse<>(INVALID_GENDER);
        }
        //나이 필수
        if (patchChildEditReq.getAge() == null) {
            return new BaseResponse<>(EMPTY_CHILD_AGE);
        }
        // 주소 필수
        if (patchChildEditReq.getAddress() == null) {
            return new BaseResponse<>(EMPTY_CHILD_ADDRESS);
        }
        try {
            PatchChildEditRes patchChildEditRes = childService.modifyChild(childIdx, patchChildEditReq);
            return new BaseResponse<>(patchChildEditRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 4. 아동 정보 삭제 api
     */
    @ResponseBody
    @PatchMapping("/status/{childIdx}")
    public BaseResponse<PatchChildDelRes> deleteChild(@PathVariable("childIdx") int childIdx) {
        try {
            PatchChildDelRes patchChildDelRes = childService.deleteChild(childIdx);
            return new BaseResponse<>(patchChildDelRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
