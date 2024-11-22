package com.example.playlistsuser.service;

import com.example.playlistsuser.data.repository.content.ContentRepository;
import com.example.playlistsuser.data.repository.playlist.PlaylistRepository;
import com.example.playlistsuser.model.Playlist;
import java.util.List;

public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public void fetchPlaylists(PlaylistRepository.PlaylistsCallback callback) {
        playlistRepository.getPlaylists(null, new PlaylistRepository.PlaylistsCallback() {
            @Override
            public void onSuccess(List<Playlist> playlists) {
                callback.onSuccess(playlists);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void createPlaylist(String playlistName, PlaylistRepository.PlaylistCreateCallback callback) {
        playlistRepository.createPlaylist(playlistName, callback);
    }

}
