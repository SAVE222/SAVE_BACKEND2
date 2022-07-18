package com.save_backend.src.suspect.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSuspectReq {
    int childIdx; // 필
    String suspectName; // 선
    String suspectGender; // 필
    String suspectAge;  // 필
    String suspectAddress; // 선
    String suspectDetailAddress; // 선
    String relationWithChild; //필
    String suspectEtc; // 선
}
