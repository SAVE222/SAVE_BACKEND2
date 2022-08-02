package com.save_backend.src.media.picture;

import com.save_backend.src.media.picture.model.PatchPictureRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PictureDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PictureDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PatchPictureRes deletePicture(Long pictureIdx) {
        String deletePictureQuery = "update picture set status = 'INACTIVE' where picture_idx = ?";

        this.jdbcTemplate.update(deletePictureQuery, pictureIdx);

        return new PatchPictureRes("삭제가 완료되었습니다.");
    }

    public boolean isPictureExist(Long pictureIdx){
        String checkExistQuery = "select exists(select picture_idx from picture where picture_idx = ? and status = 'ACTIVE')";

        return this.jdbcTemplate.queryForObject(checkExistQuery, boolean.class, pictureIdx);
    }

    public String getPictureKey(Long pictureIdx){
        String getPictureKeyQuery = "select pic_path from picture where picture_idx = ?";

        return this.jdbcTemplate.queryForObject(getPictureKeyQuery, String.class, pictureIdx);
    }
}
