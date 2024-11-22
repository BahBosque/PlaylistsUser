package com.example.playlistsuser.data.repository.auth;

import androidx.annotation.NonNull;

import com.example.playlistsuser.config.ApiConfig;
import com.example.playlistsuser.data.repository.UnsafeOkHttpClient;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class AuthRepositoryImpl implements AuthRepository {
    private final OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @Override
    public void login(String email, String password, AuthCallback callback) {
        String url = ApiConfig.getInstance().getAuthUrl();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("senha", password);
        } catch (Exception e) {
            callback.onError("Erro ao criar o corpo da requisição");
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().string();
                    try {
                        callback.onSuccess(token);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onError("Erro de autenticação: " + response.message());
                }
            }
        });
    }
}
