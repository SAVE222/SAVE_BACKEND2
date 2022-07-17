package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.child.model.GetChildInfoRes;
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
}
