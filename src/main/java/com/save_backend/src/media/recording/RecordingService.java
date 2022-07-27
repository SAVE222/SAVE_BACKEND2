package com.save_backend.src.media.recording;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.media.S3Service;
import com.save_backend.src.media.recording.entity.Recording;
import com.save_backend.src.media.recording.model.GetRecordingRes;
import com.save_backend.src.media.recording.model.PatchRecordingRes;
import com.save_backend.src.media.recording.model.PostRecordingReq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecordingService {

    private String path = "https://dby56rj67jahx.cloudfront.net/recording/";

    private final S3Service s3Service;
    private final RecordingDbRepository recordingDbRepository;
    private final RecordingDao recordingDao;

    @Autowired
    public RecordingService(S3Service s3Service, RecordingDbRepository recordingDbRepository, RecordingDao recordingDao) {
        this.s3Service = s3Service;
        this.recordingDbRepository = recordingDbRepository;
        this.recordingDao = recordingDao;
    }


    public Long upload(MultipartFile recording, String recordingName, PostRecordingReq postRecordingReq) throws BaseException {
        try{
            String location = s3Service.upload(recording, "static/recording");
            return recordingDbRepository.save(new Recording(location, recordingName, postRecordingReq.getRecAbuseIdx(), postRecordingReq.getRecChildIdx())).getRecordingIdx();
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.UPLOAD_FAIL_RECORDING);
        }
    }

    public PatchRecordingRes deleteRecording(Long recordingIdx) throws BaseException {
        //validation
        if(!recordingDao.isRecordingExist(recordingIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_RECORDING);
        }

        try{
            return recordingDao.deleteRecording(recordingIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DELETE_FAIL_RECORDING);
        }
    }

    public String getRecordingPath(Long recordingIdx) throws BaseException {
        //validation
        if(!recordingDao.isRecordingExist(recordingIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_RECORDING);
        }

        try{
            String fileName = FilenameUtils.getName(recordingDao.getRecordingKey(recordingIdx));
            return path + fileName;
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
