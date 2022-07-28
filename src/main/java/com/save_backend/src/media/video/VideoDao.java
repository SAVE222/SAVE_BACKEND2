package com.save_backend.src.media.video;

import com.save_backend.src.media.video.model.PatchVideoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VideoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public PatchVideoRes deleteVideo(Long videoIdx) {
        String deleteVideoQuery = "update video set status = 'INACTIVE' where video_idx = ?";

        this.jdbcTemplate.update(deleteVideoQuery, videoIdx);

        return new PatchVideoRes("삭제가 완료되었습니다.");
    }

    public boolean isVideoExist(Long videoIdx) {
        String checkExistQuery = "select exists(select video_idx from video where video_idx = ? and status = 'ACTIVE')";

        return this.jdbcTemplate.queryForObject(checkExistQuery, boolean.class, videoIdx);
    }

    public String getVideoKey(Long videoIdx) {
        String getVideoKeyQuery = "select vid_path from video where video_idx = ?";

        return this.jdbcTemplate.queryForObject(getVideoKeyQuery, String.class, videoIdx);
    }
}
