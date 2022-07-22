package com.save_backend.src.suspect.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSuspectRes {
    private String suspectName;
    private String suspectGender;
    private String suspectAge;
    private String suspectAddress;
    private String suspectDetailAddress;
    private String suspectEtc;
}
