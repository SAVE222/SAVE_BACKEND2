package com.save_backend.src.get.child.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GetChildListRes {

    private int childIdx;
    private String name;
    private String gender;
    private String age;
    private String address;
    private String detailAddress;
    private Date createAt;
}
