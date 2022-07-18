package com.save_backend.src.suspect;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/suspect")
public class SuspectController {

    @Autowired
    private final SuspectProvider suspectProvider;
    @Autowired
    private final SuspectService suspectService;


    public SuspectController(SuspectProvider suspectProvider, SuspectService suspectService) {
        this.suspectProvider = suspectProvider;
        this.suspectService = suspectService;
    }


}
