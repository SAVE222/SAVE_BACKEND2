package com.save_backend.src.suspect.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchSuspectRes {
    // 수정된 후 모든 정보에 대한 결과 반환
    private int suspectIdx;
    private String suspectName;
    private String suspectGender;
    private String suspectAge;
    private String suspectAddress;
    private String suspectDetailAddress;
    private String relationWithChild;
    private String suspectEtc;
}
