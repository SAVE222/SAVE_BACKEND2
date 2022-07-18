package com.save_backend.src.suspect.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSuspectRes {
    String suspectName;
    String suspectGender;
    String suspectAge;
    String suspectAddress;
    String suspectDetailAddress;
    String suspectEtc;
}
