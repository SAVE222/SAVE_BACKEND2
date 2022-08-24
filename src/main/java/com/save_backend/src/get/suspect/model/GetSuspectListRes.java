package com.save_backend.src.get.suspect.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetSuspectListRes {

    private int suspectIdx;
    private String name;
    private String gender;
    private String age;
    private String address;
    private String detailAddress;
    private String relation;
}
