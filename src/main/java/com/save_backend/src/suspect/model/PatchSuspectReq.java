package com.save_backend.src.suspect.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchSuspectReq {
    // 수정가능한 모든 정보에 대한 필드
    String suspectName;
    String suspectGender;
    String suspectAge;
    String suspectAddress;
    String suspectDetailAddress;
    String relationWithChild;
    String suspectEtc;
}
