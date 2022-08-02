package com.save_backend.src.media.recording.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "recording")
@Getter
@Setter
@NoArgsConstructor
public class Recording {

    @Id
    @Column(name = "recording_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long recordingIdx;

    @Column(name = "content")
    @Lob
    byte[] content;

    @Column(name = "rec_path")
    String recPath;

    @Column(name = "recording_name")
    String recordingName;

    @Column(name = "rec_abuse_idx")
    int recAbuseIdx;

    @Column(name = "rec_child_idx")
    int recChildIdx;

    public Recording(String path, String recordingName, int recAbuseIdx, int recChildIdx){
        this.recPath = path;
        this.recordingName = recordingName;
        this.recAbuseIdx = recAbuseIdx;
        this.recChildIdx = recChildIdx;
    }
}
