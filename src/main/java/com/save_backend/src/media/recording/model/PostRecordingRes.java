package com.save_backend.src.media.recording.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostRecordingRes {

    private List<Long> recordingIdx;
    private String completeMessage;

    public PostRecordingRes(){
        this.recordingIdx = new ArrayList<>();
    }

    public void setRecordingIdx(Long recordingIdx) {
        this.recordingIdx.add(recordingIdx);
    }
}
