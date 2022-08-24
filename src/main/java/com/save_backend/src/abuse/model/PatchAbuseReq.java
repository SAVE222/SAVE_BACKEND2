package com.save_backend.src.abuse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchAbuseReq {
    private String date;
    private String time;
    private String place;
    private String detail;
    private String etc;  //선택
    private String type;
    private int suspectIdx;
}
