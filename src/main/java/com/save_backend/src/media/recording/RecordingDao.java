package com.save_backend.src.media.recording;

import com.save_backend.src.media.recording.model.PatchRecordingRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecordingDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RecordingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public PatchRecordingRes deleteRecording(Long recordingIdx) {
        String deleteRecordingQuery = "update recording set status = 'INACTIVE' where recording_idx = ?";

        this.jdbcTemplate.update(deleteRecordingQuery, recordingIdx);

        return new PatchRecordingRes("삭제가 완료되었습니다.");
    }

    public boolean isRecordingExist(Long recordingIdx) {
        String checkExistQuery = "select exists(select recording_idx from recording where recording_idx = ? and status = 'ACTIVE')";

        return this.jdbcTemplate.queryForObject(checkExistQuery, boolean.class, recordingIdx);
    }

    public String getRecordingKey(Long recordingIdx) {
        String getRecordingKeyQuery = "select rec_path from recording where recording_idx = ?";

        return this.jdbcTemplate.queryForObject(getRecordingKeyQuery, String.class, recordingIdx);
    }
}
