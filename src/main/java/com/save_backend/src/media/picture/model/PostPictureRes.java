package com.save_backend.src.media.picture.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPictureRes {

    private List<Long> pictureIdx;
    private String completeMessage;

    public PostPictureRes(){
        this.pictureIdx = new ArrayList<>();
    }

    public void setPictureIdx(Long pictureIdx) {
        this.pictureIdx.add(pictureIdx);
    }
}
