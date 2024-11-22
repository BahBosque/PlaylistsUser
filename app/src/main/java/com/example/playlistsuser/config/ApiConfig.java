package com.example.playlistsuser.config;

public class ApiConfig {
    private static ApiConfig instance;
    private String baseUrl;

    private ApiConfig() {
        this.baseUrl = "http://10.0.2.2:8000/api/v1/";
    }

    public static ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    public String getAuthUrl() {
        return baseUrl + "auth/login";
    }

    public String getPlaylistsUrl() {
        return baseUrl + "playlists";
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
