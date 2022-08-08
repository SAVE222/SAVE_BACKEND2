package com.save_backend.src.get.abuse;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.get.abuse.model.GetAbuseListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class AbuseListController {
    private final AbuseListProvider abuseListProvider;


    @GetMapping("/abuse/{childIdx}")
    public BaseResponse<List<GetAbuseListRes>> getChildList(@PathVariable int childIdx){
        try{
            List<GetAbuseListRes> result = abuseListProvider.getAbuseList(childIdx);
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
