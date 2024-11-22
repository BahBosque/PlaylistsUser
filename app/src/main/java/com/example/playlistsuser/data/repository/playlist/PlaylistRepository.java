package com.example.playlistsuser.data.repository.playlist;

import com.example.playlistsuser.model.Playlist;

import org.json.JSONObject;

import java.util.List;

public interface PlaylistRepository {

    void createPlaylist(String playlistName, PlaylistCreateCallback callback);

    interface PlaylistsCallback {
        void onSuccess(List<Playlist> playlists);
        void onError(String errorMessage);
    }

    interface PlaylistCreateCallback {
        void onSuccess(Playlist newPlaylist); // Agora usa Playlist
        void onError(String errorMessage);
    }

    void getPlaylists(String authToken, PlaylistsCallback callback);
}
