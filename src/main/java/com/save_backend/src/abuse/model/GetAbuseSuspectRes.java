package com.save_backend.src.abuse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAbuseSuspectRes {
    private String suspectName;
    private String suspectGender;
    private String suspectAge;
    private String suspectAddress;
}
