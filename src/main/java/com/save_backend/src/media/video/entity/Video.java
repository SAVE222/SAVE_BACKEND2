package com.save_backend.src.media.video.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "video")
@Getter
@Setter
@NoArgsConstructor
public class Video {

    @Id
    @Column(name = "video_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long videoIdx;

    @Column(name = "content")
    @Lob
    byte[] content;

    @Column(name = "vid_path")
    String vidPath;

    @Column(name = "video_name")
    String videoName;

    @Column(name = "vid_abuse_idx")
    int vidAbuseIdx;

    @Column(name = "vid_child_idx")
    int vidChildIdx;

    public Video(String path, String videoName, int vidAbuseIdx, int vidChildIdx) {
        this.vidPath = path;
        this.videoName = videoName;
        this.vidAbuseIdx = vidAbuseIdx;
        this.vidChildIdx = vidChildIdx;
    }
}
