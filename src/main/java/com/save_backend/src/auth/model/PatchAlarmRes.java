package com.save_backend.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchAlarmRes {
    private int userIdx;
    private Boolean isAlarm;
}
