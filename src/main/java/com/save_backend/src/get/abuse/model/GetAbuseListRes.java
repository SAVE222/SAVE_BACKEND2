package com.save_backend.src.get.abuse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GetAbuseListRes {
    private int abuseIdx;
    private int childIdx;
    private String date;
    private String time;
    private String place;
    private Date create_date;
}
