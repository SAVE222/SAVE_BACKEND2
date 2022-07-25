package com.save_backend.src.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthProvider authProvider;
    private final AuthService authService;



    public AuthController(AuthProvider authProvider, AuthService authService){
        this.authProvider = authProvider;
        this.authService = authService;
    }


}
