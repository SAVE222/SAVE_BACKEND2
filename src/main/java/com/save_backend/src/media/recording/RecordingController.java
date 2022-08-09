package com.save_backend.src.media.recording;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponse;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.media.recording.model.PatchRecordingRes;
import com.save_backend.src.media.recording.model.PostRecordingReq;
import com.save_backend.src.media.recording.model.PostRecordingRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recording")
public class RecordingController {

    private final RecordingService recordingService;

    @Autowired
    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }


    /**
     * [POST]녹음파일 업로드 API
     * (녹음파일 추가하는 경우[수정]에도 해당 API 이용)
     */
    @PostMapping("")
    public BaseResponse<PostRecordingRes> uploadRecording(@RequestPart MultipartFile[] recording, @RequestPart PostRecordingReq postRecordingReq){
        try{
            if(recording == null){
                return new BaseResponse<>(BaseResponseStatus.EMPTY_FILE);
            }

            PostRecordingRes result = new PostRecordingRes();
            for(MultipartFile p : recording){
                Long idx = recordingService.upload(p, p.getOriginalFilename(), postRecordingReq);
                result.setRecordingIdx(idx);
            }
            result.setCompleteMessage("녹음파일 업로드가 완료되었습니다.");

            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }



    /**
     * [PATCH]녹음파일 수정 API - 삭제
     */
    @PatchMapping("/status/{recordingIdx}")
    public BaseResponse<PatchRecordingRes> deleteRecording(@PathVariable Long recordingIdx){
        try{
            PatchRecordingRes result = recordingService.deleteRecording(recordingIdx);
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
