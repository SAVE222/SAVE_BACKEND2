package com.save_backend.src.child.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchChildRes {
    private String name;
    private String gender;
    private String age;
    private String address;
    private String detailAddress;
    private String deleteChild;
}
