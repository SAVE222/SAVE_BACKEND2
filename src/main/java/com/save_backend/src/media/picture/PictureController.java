package com.save_backend.src.media.picture;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.media.picture.model.PatchPictureRes;
import com.save_backend.src.media.picture.model.PostPictureReq;
import com.save_backend.src.media.picture.model.PostPictureRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/picture")
public class PictureController {

    private final PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }


    /**
     * [POST]사진 업로드 API
     * (사진 추가하는 경우[수정]에도 해당 API 이용)
     */
    @PostMapping("")
    public BaseResponse<PostPictureRes> uploadPicture(@RequestPart MultipartFile[] picture, @RequestPart PostPictureReq postPictureReq){
        try{
            if(picture == null){
                return new BaseResponse<>(BaseResponseStatus.EMPTY_FILE);
            }

            PostPictureRes result = new PostPictureRes();
            for(MultipartFile p : picture){
                Long idx = pictureService.upload(p, p.getOriginalFilename(), postPictureReq);
                result.setPictureIdx(idx);
            }
            result.setCompleteMessage("사진 업로드가 완료되었습니다.");

            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /**
     * [PATCH]사진 수정 API - 삭제
     */
    @PatchMapping("/status/{pictureIdx}")
    public BaseResponse<PatchPictureRes> deletePicture(@PathVariable Long pictureIdx){
        try{
            PatchPictureRes result = pictureService.deletePicture(pictureIdx);
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
