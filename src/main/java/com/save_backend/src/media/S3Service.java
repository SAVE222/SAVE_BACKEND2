package com.save_backend.src.media;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.save_backend.config.exception.BaseException;
import com.save_backend.config.response.BaseResponseStatus;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Autowired
    public S3Service(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }


    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException());

        return upload(uploadFile, dirName);
    }


    //MultipartFile을 File로 변환
    private Optional<File> convert(MultipartFile multipartFile) throws IOException{
        File uploadFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        if(uploadFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(uploadFile);){
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(uploadFile);
        }

        return Optional.empty();
    }


    private String upload(File uploadFile, String dirName){
        String fileName = dirName + "/" + UUID.randomUUID() + "." + FilenameUtils.getExtension(uploadFile.getName());

        String uploadFileUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        return uploadFileUrl;
    }


    //s3 버킷에 파일 업로드
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    //로컬에 생성한 파일 삭제
    private void removeNewFile(File targetFile){
        if(targetFile.delete()){
            return;
        }
    }


    public byte[] download(String folderName, String key){
        S3Object obj = amazonS3Client.getObject(bucket, folderName+"/"+key);
        S3ObjectInputStream stream = obj.getObjectContent();

        try{
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();

            return content;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
