package com.save_backend.src.suspect.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchSuspectRes {
    // 수정된 후 모든 정보에 대한 결과 반환
    int suspectIdx;
    String suspectName;
    String suspectGender;
    String suspectAge;
    String suspectAddress;
    String suspectDetailAddress;
    String relationWithChild;
    String suspectEtc;
}
