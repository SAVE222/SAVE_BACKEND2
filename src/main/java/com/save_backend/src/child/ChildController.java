package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.child.model.GetChildInfoRes;
import com.save_backend.src.child.model.PostChildReq;
import com.save_backend.src.child.model.PostChildRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.save_backend.config.response.BaseResponseStatus.*;

@RestController
@RequestMapping("/child")
public class ChildController {

    @Autowired
    private final ChildProvider childProvider;
    @Autowired
    private final ChildService childService;


    public ChildController(ChildProvider childProvider, ChildService childService){
        this.childProvider = childProvider;
        this.childService = childService;
    }


    //특정 아동 조회 api
    @ResponseBody
    @GetMapping("/{childIdx}")
    public BaseResponse<GetChildInfoRes> getChildInfoByIdx(@PathVariable("childIdx") int childIdx) {
        try{

            GetChildInfoRes getChildInfoRes = childProvider.getChildInfoByIdx(childIdx);
            return new BaseResponse<>(getChildInfoRes);
        } catch(BaseException exception){
            System.out.println("exception = " + exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //아동 생성 api
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostChildRes> createChild(@RequestBody PostChildReq postChildReq) {

        try {
            if (postChildReq.getUserIdx() == 0) {
                return new BaseResponse<>(POST_CHILD_EMPTY_USERIDX);
            }
            if (postChildReq.getName() == null) {
                return new BaseResponse<>(POST_CHILD_EMPTY_NAME);
            }
            if (postChildReq.getGender() == null) {
                return new BaseResponse<>(POST_CHILD_EMPTY_GENDER);
            }
            if (postChildReq.getAge() == null) {
                return new BaseResponse<>(POST_CHILD_EMPTY_AGE);
            }
            if (postChildReq.getAddress() == null) {
                return new BaseResponse<>(POST_CHILD_EMPTY_ADDRESS);
            }
            if (postChildReq.getDetailAddress() == null) {
                return new BaseResponse<>(POST_CHILD_EMPTY_DETAIL_ADDRESS);
            }

            PostChildRes postChildRes = childService.createChild(postChildReq);
            return new BaseResponse<>(postChildRes);
        } catch (BaseException exception) {
            System.out.println("exception = " + exception);
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
