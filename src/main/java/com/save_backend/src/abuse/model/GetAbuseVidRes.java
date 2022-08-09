package com.save_backend.src.abuse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAbuseVidRes {
    private String videoPath;
    private String thumbnailPath;
}
