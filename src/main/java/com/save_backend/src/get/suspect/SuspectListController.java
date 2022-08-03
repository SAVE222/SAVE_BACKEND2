package com.save_backend.src.get.suspect;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.src.get.suspect.model.GetSuspectListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class SuspectListController {

    private final SuspectListProvider suspectListProvider;


    @GetMapping("/suspect/{childIdx}")
    public BaseResponse<List<GetSuspectListRes>> getSuspectList(@PathVariable int childIdx){
        try{
            List<GetSuspectListRes> result = suspectListProvider.getSuspectList(childIdx);
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
