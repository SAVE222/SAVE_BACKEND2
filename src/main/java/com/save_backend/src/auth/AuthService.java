package com.save_backend.src.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthDao authDao;
    private final AuthProvider authProvider;


    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider) {
        this.authDao = authDao;
        this.authProvider = authProvider;
    }



}
