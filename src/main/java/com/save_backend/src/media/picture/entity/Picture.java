package com.save_backend.src.media.picture.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "picture")
@Getter
@Setter
@NoArgsConstructor
public class Picture {

    @Id
    @Column(name = "picture_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long pictureIdx;

    @Column(name = "content")
    @Lob
    byte[] content;

    @Column(name = "pic_path")
    String picPath;

    @Column(name = "picture_name")
    String pictureName;

    @Column(name = "pic_abuse_idx")
    int picAbuseIdx;

    @Column(name = "pic_child_idx")
    int picChildIdx;

    public Picture(String path, String pictureName, int picAbuseIdx, int picChildIdx) {
        this.picPath = path;
        this.pictureName = pictureName;
        this.picAbuseIdx = picAbuseIdx;
        this.picChildIdx = picChildIdx;
    }
}
