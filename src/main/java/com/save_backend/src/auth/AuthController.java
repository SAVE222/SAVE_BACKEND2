package com.save_backend.src.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthProvider authProvider;
    private final AuthService authService;


    @Autowired
    public AuthController(AuthProvider authProvider, AuthService authService){
        this.authProvider = authProvider;
        this.authService = authService;
    }


}
