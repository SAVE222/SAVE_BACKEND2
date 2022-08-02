package com.save_backend.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchAuthRes {
    private int userIdx;
    private String newPassword;
    private String successMessage;
}
