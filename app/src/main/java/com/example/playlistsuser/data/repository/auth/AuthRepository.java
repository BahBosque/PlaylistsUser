package com.example.playlistsuser.data.repository.auth;

import org.json.JSONException;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);

    interface AuthCallback {
        void onSuccess(String token) throws JSONException;
        void onError(String errorMessage);
    }
}
