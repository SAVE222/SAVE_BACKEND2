package com.save_backend.src.suspect;

import org.springframework.stereotype.Service;

@Service
public class SuspectProvider {

    private final SuspectDao suspectDao;

    public SuspectProvider(SuspectDao suspectDao) {
        this.suspectDao = suspectDao;
    }
}
