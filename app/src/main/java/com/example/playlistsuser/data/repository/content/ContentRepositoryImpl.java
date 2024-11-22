package com.example.playlistsuser.data.repository.content;

import androidx.annotation.NonNull;

import com.example.playlistsuser.data.repository.SessionManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentRepositoryImpl implements ContentRepository {

    private final OkHttpClient client;
    private final SessionManager sessionManager;

    public ContentRepositoryImpl(SessionManager sessionManager, OkHttpClient client) {
        this.sessionManager = sessionManager;
        this.client = client;
    }

    @Override
    public void getAllContents(ContentCallback callback) {
        String token = sessionManager.getAuthToken();
        String url = "http://10.0.2.2:8000/api/v1/conteudos";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray contentArray = new JSONArray(response.body().string());
                        List<JSONObject> contents = new ArrayList<>();
                        for (int i = 0; i < contentArray.length(); i++) {
                            contents.add(contentArray.getJSONObject(i));
                        }
                        callback.onSuccess(contents);
                    } catch (JSONException e) {
                        callback.onError("Erro ao processar resposta do servidor");
                    }
                } else {
                    callback.onError("Erro na resposta do servidor: " + response.message());
                }
            }
        });
    }

    @Override
    public void addContentToPlaylist(int playlistId, int contentId, ContentAddCallback callback) {
        String token = sessionManager.getAuthToken();
        String url = "http://10.0.2.2:8000/api/v1/playlists/" + playlistId + "/conteudos/" + contentId;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Erro ao adicionar conteúdo: " + response.message());
                }
            }
        });
    }

    @Override
    public void removeContent(int playlistId, int contentId, ContentRemoveCallback callback) {
        String token = sessionManager.getAuthToken();
        String url = "http://10.0.2.2:8000/api/v1/playlists/" + playlistId + "/conteudos/" + contentId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Erro do servidor: " + response.message());
                }
            }
        });
    }

    @Override
    public void getPlaylistContents(int playlistId, ContentCallback callback) {
        String token = sessionManager.getAuthToken();
        String url = "http://10.0.2.2:8000/api/v1/playlists/" + playlistId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject playlistResponse = new JSONObject(response.body().string());
                        JSONArray contentArray = playlistResponse.optJSONArray("conteudos");
                        List<JSONObject> contents = new ArrayList<>();

                        if (contentArray != null) {
                            for (int i = 0; i < contentArray.length(); i++) {
                                contents.add(contentArray.getJSONObject(i));
                            }
                        }

                        callback.onSuccess(contents);
                    } catch (JSONException e) {
                        callback.onError("Erro ao processar resposta do servidor");
                    }
                } else {
                    callback.onError("Erro na resposta do servidor: " + response.message());
                }
            }
        });
    }

}
