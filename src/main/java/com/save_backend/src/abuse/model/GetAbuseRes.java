package com.save_backend.src.abuse.model;

import com.save_backend.src.suspect.model.GetSuspectRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAbuseRes {
    private int abuseIdx;
    private String date;
    private String time;
    private String place;
    private String type;
    private String detail;
    private String etc;
    private String createdAt;
    private List<GetAbusePicRes> picture;
    private List<GetAbuseVidRes> video;
    private List<GetAbuseRecRes> recording;
    private GetAbuseSuspectRes suspect;
}
