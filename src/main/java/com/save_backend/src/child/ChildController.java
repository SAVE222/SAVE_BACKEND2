package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.child.model.GetChildInfoRes;
import com.save_backend.src.child.model.PostChildReq;
import com.save_backend.src.child.model.PostChildRes;
import com.save_backend.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/child")
public class ChildController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ChildProvider childProvider;
    @Autowired
    private final ChildService childService;
    @Autowired
    private final JwtService jwtService;


    public ChildController(ChildProvider childProvider, ChildService childService){
        this.childProvider = childProvider;
        this.childService = childService;
    }


    //특정 아동 조회 api
    @ResponseBody
    @GetMapping("/{childIdx}")
    public BaseResponse<GetChildInfoRes> getChildInfoByIdx(@PathVariable("childIdx")int childIdx) {
        try{

            GetChildInfoRes getChildInfoRes = childProvider.getChildInfoByIdx(childIdx);
            return new BaseResponse<>(getChildInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //아동 생성 api
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostChildRes> createChild(@RequestBody PostChildReq postChildReq) {
        if(postChildReq.getName() == null){
            return new BaseResponse<>(POST_CHILD_EMPTY_NAME);
        }
        if(postChildReq.getGender() == null){
            return new BaseResponse<>(POST_CHILD_EMPTY_GENDER);
        }
        if(postChildReq.getAge() == null){
            return new BaseResponse<>(POST_CHILD_EMPTY_AGE);
        }
        if(postChildReq.getAge() == null){
            return new BaseResponse<>(POST_CHILD_EMPTY_AGE);
        }
        if(postChildReq.getAddress() == null){
            return new BaseResponse<>(POST_CHILD_EMPTY_ADDRESS);
        }if(postChildReq.getDetailAddress() == null){
            return new BaseResponse<>(POST_CHILD_EMPTY_DETAIL_ADDRESS);
        }

        try{
            int userIdxByJwt = jwtService.getUserIdx();
            PostChildRes postChildRes = childService.createPost(userIdxByJwt,postChildReq);
            return new BaseResponse<>(postChildRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
