package com.save_backend.src.abuse.model;

import com.save_backend.src.suspect.model.PostSuspectReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostAbuseReq {
    private int childIdx;
    private String date;
    private String time;
    private String place;
    private String detail;
    private String etc;
    private List<String> type;
    private List<PostAbuseSuspectReq> suspect;
}
