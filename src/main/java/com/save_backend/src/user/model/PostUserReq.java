package com.save_backend.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {

    private int isSnsAuth;
    private String name;
    private String phone;
    private String email;
    private String password;
}
