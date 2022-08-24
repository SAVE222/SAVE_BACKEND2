package com.save_backend.src.user.model;

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
