package com.save_backend.src.abuse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAbuseRes {
    private String date;
    private String time;
    private String place;
    private String type;
    private String detail;
    private String etc;
}
