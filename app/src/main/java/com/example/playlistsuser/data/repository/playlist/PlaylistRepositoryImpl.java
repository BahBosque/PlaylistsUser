package com.example.playlistsuser.data.repository.playlist;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.playlistsuser.config.ApiConfig;
import com.example.playlistsuser.data.repository.SessionManager;
import com.example.playlistsuser.data.repository.UnsafeOkHttpClient;
import com.example.playlistsuser.data.repository.playlist.PlaylistRepository;
import com.example.playlistsuser.model.Playlist;
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

public class PlaylistRepositoryImpl implements PlaylistRepository {
    private final OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    private final String playlistsUrl;
    private final SessionManager sessionManager;

    public PlaylistRepositoryImpl(Context context) {
        playlistsUrl = ApiConfig.getInstance().getPlaylistsUrl();
        sessionManager = new SessionManager(context);
    }

    @Override
    public void getPlaylists(String authToken, PlaylistRepository.PlaylistsCallback callback) {
        String token = sessionManager.getAuthToken();

        Request request = new Request.Builder()
                .url(playlistsUrl)
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
                    List<Playlist> playlists = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            JSONArray content = json.getJSONArray("conteudos");
                            String title = json.getString("nome");
                            int id = json.getInt("id");
                            playlists.add(new Playlist(id, title, content));
                        }
                        callback.onSuccess(playlists);
                    } catch (Exception e) {
                        callback.onError("Erro ao processar dados: " + e.getMessage());
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Corpo de erro vazio";
                    callback.onError("Erro de autenticação: " + response.message() + " | Detalhes: " + errorBody);
                }
            }
        });
    }

    @Override
    public void createPlaylist(String playlistName, PlaylistCreateCallback callback) {
        String token = sessionManager.getAuthToken();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nome", playlistName);
        } catch (JSONException e) {
            callback.onError("Erro ao criar o corpo da requisição");
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(playlistsUrl)
                .post(body)
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
                        JSONObject json = new JSONObject(response.body().string());
                        Playlist newPlaylist = new Playlist(
                                json.getInt("id"),
                                json.getString("nome"),
                                json.optJSONArray("conteudos")
                        );
                        callback.onSuccess(newPlaylist);
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
