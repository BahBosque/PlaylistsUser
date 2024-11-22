package com.example.playlistsuser.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.playlistsuser.data.repository.SessionManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class ApiService {
    private final OkHttpClient client;

    public ApiService(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        String token = sessionManager.getAuthToken();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + token);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
