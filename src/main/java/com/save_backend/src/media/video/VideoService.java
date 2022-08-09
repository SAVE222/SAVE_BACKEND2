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

    private final String path = "https://dby56rj67jahx.cloudfront.net/";

    private final S3Service s3Service;
    private final VideoDbRepository videoDbRepository;
    private final VideoDao videoDao;

    @Autowired
    public VideoService(S3Service s3Service, VideoDbRepository videoDbRepository, VideoDao videoDao) {
        this.s3Service = s3Service;
        this.videoDbRepository = videoDbRepository;
        this.videoDao = videoDao;
    }


    public Long upload(MultipartFile video, MultipartFile thumbnail, String videoName, PostVideoReq postVideoReq) throws BaseException {
        try{
            String videoPath = s3Service.upload(video, "static/video");
            String thumbnailPath = s3Service.upload(thumbnail, "static/thumbnail");
            return videoDbRepository.save(new Video(path+"video/"+FilenameUtils.getName(videoPath), videoName, path+"thumbnail/"+FilenameUtils.getName(thumbnailPath), postVideoReq.getVidAbuseIdx(), postVideoReq.getVidChildIdx())).getVideoIdx();
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.UPLOAD_FAIL_VIDEO);
        }
    }

    public PatchVideoRes deleteVideo(Long videoIdx) throws BaseException {
        if(!videoDao.isVideoExist(videoIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_VIDEO);
        }

        try{
            return videoDao.deleteVideo(videoIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DELETE_FAIL_VIDEO);
        }
    }
}
