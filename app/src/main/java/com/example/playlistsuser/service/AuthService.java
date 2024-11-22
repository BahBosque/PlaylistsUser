package com.example.playlistsuser.service;

import com.example.playlistsuser.data.repository.auth.AuthRepository;

public class AuthService {
    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void login(String email, String password, AuthRepository.AuthCallback callback) {
        authRepository.login(email, password, callback);
    }
}
