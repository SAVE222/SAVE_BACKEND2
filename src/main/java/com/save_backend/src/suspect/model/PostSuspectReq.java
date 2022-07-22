package com.save_backend.src.suspect.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSuspectReq {
    private int childIdx; // 필
    private String suspectName; // 선
    private String suspectGender; // 필
    private String suspectAge;  // 필
    private String suspectAddress; // 선
    private String suspectDetailAddress; // 선
    private String relationWithChild; //필
    private String suspectEtc; // 선
}
