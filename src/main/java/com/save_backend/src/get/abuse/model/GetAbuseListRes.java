package com.save_backend.src.get.abuse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAbuseListRes {
    private int abuseIdx;
    private int childIdx;
    private String date;
    private String time;
    private String place;
    private String create_date;
}
