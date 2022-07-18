package com.save_backend.src.suspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuspectService {
    private final SuspectProvider suspectProvider;
    private final SuspectDao suspectDao;

    @Autowired
    public SuspectService(SuspectProvider suspectProvider, SuspectDao suspectDao) {
        this.suspectProvider = suspectProvider;
        this.suspectDao = suspectDao;
    }
}
