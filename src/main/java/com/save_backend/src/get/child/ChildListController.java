package com.save_backend.src.get.child;


import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.get.child.model.GetChildListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class ChildListController {

    private final ChildListProvider childListProvider;


    @GetMapping("/child/{userIdx}")
    public BaseResponse<List<GetChildListRes>> getChildList(@PathVariable int userIdx){
        try{
            List<GetChildListRes> result = childListProvider.getChildList(userIdx);
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
