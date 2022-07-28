package com.save_backend.src.media.video.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoRes {

    private List<Long> videoIdx;
    private String completeMessage;

    public PostVideoRes() { this.videoIdx = new ArrayList<>(); }

    public void setVideoIdx(Long videoIdx) { this.videoIdx.add(videoIdx); }
}
