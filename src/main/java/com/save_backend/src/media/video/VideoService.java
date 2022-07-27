package com.save_backend.src.media.video;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.media.S3Service;
import com.save_backend.src.media.video.entity.Video;
import com.save_backend.src.media.video.model.PatchVideoRes;
import com.save_backend.src.media.video.model.PostVideoReq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    private String path = "https://dby56rj67jahx.cloudfront.net/video/";

    private final S3Service s3Service;
    private final VideoDbRepository videoDbRepository;
    private final VideoDao videoDao;

    @Autowired
    public VideoService(S3Service s3Service, VideoDbRepository videoDbRepository, VideoDao videoDao) {
        this.s3Service = s3Service;
        this.videoDbRepository = videoDbRepository;
        this.videoDao = videoDao;
    }


    public Long upload(MultipartFile video, String videoName, PostVideoReq postVideoReq) throws BaseException {
        try{
            String location = s3Service.upload(video, "static/video");
            return videoDbRepository.save(new Video(location, videoName, postVideoReq.getVidAbuseIdx(), postVideoReq.getVidChildIdx())).getVideoIdx();
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.UPLOAD_FAIL_VIDEO);
        }
    }

    public PatchVideoRes deleteVideo(Long videoIdx) throws BaseException {
        //validation
        if(!videoDao.isVideoExist(videoIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_VIDEO);
        }

        try{
            return videoDao.deleteVideo(videoIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DELETE_FAIL_VIDEO);
        }
    }

    public String getVideoPath(Long videoIdx) throws BaseException {
        //validation
        if(!videoDao.isVideoExist(videoIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_VIDEO);
        }

        try{
            String fileName = FilenameUtils.getName(videoDao.getVideoKey(videoIdx));
            return path + fileName;
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
