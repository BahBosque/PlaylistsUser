package com.example.playlistsuser.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String PREF_NAME = "auth_pref";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAuthToken(String token) throws JSONException {
        SharedPreferences.Editor editor = prefs.edit();
        JSONObject jsonObject = new JSONObject(token);
        editor.putString(KEY_AUTH_TOKEN, jsonObject.getString("token"));
        editor.apply();
    }

    public String getAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    public void clearAuthToken() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
    }
}
