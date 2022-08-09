package com.save_backend.src.media.picture;

import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import com.save_backend.src.media.S3Service;
import com.save_backend.src.media.picture.entity.Picture;
import com.save_backend.src.media.picture.model.PatchPictureRes;
import com.save_backend.src.media.picture.model.PostPictureReq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureService {

    private final String path = "https://dby56rj67jahx.cloudfront.net/picture/";

    private final S3Service s3Service;
    private final PictureDbRepository pictureDbRepository;
    private final PictureDao pictureDao;

    @Autowired
    public PictureService(S3Service s3Uploader, PictureDbRepository pictureDbRepository, PictureDao pictureDao) {
        this.s3Service = s3Uploader;
        this.pictureDbRepository = pictureDbRepository;
        this.pictureDao = pictureDao;
    }


    Long upload(MultipartFile picture, String pictureName, PostPictureReq postPictureReq) throws BaseException{
        try{
            String location = s3Service.upload(picture, "static/picture");
            return pictureDbRepository.save(new Picture(path+FilenameUtils.getName(location), pictureName, postPictureReq.getPicAbuseIdx(), postPictureReq.getPicChildIdx())).getPictureIdx();
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.UPLOAD_FAIL_IMAGE);
       }
    }


    public PatchPictureRes deletePicture(Long pictureIdx) throws BaseException {
        if(!pictureDao.isPictureExist(pictureIdx)){
            throw new BaseException(BaseResponseStatus.NOT_EXIST_PICTURE);
        }

        try{
            return pictureDao.deletePicture(pictureIdx);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.DELETE_FAIL_IMAGE);
        }
    }
}
