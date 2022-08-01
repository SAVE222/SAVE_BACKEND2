package com.save_backend.src.abuse.model;

import com.save_backend.src.suspect.model.GetSuspectRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    private List<GetAbusePicRes> pictureIdx;
    private List<GetAbuseVidRes>  videoIdx;
    private List<GetAbuseRecRes> recordingIdx;
    private List<GetAbuseSuspectRes> suspect;
}
