package com.save_backend.src.media.video;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.media.video.model.PatchVideoRes;
import com.save_backend.src.media.video.model.PostVideoReq;
import com.save_backend.src.media.video.model.PostVideoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    /**
     * [POST]동영상 업로드 API
     * (동영상 추가하는 경우[수정]에도 해당 API 이용)
     */
    @PostMapping("")
    public BaseResponse<PostVideoRes> uploadVideo(@RequestPart MultipartFile[] video, MultipartFile thumbnail, @RequestPart PostVideoReq postVideoReq){
        try{
            if(video == null){
                return new BaseResponse<>(BaseResponseStatus.EMPTY_FILE);
            }

            PostVideoRes result = new PostVideoRes();
            for(MultipartFile p : video){
                Long videoIdx = videoService.upload(p, thumbnail, p.getOriginalFilename(), postVideoReq);
                result.setVideoIdx(videoIdx);
            }
            result.setCompleteMessage("동영상 업로드가 완료되었습니다.");

            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * [PATCH]동영상 수정 API - 삭제
     */
    @PatchMapping("/status/{videoIdx}")
    public BaseResponse<PatchVideoRes> deleteVideo(@PathVariable Long videoIdx){
        try{
            PatchVideoRes result = videoService.deleteVideo(videoIdx);
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
