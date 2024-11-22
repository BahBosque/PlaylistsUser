package com.example.playlistsuser.ui.login;

import com.example.playlistsuser.data.repository.auth.AuthRepository;
import com.example.playlistsuser.service.AuthService;

public class LoginViewModel {
    private final AuthService authService;

    public LoginViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void login(String email, String password, AuthRepository.AuthCallback callback) {
        authService.login(email, password, callback);
    }
}
