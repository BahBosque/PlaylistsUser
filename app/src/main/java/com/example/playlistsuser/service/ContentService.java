package com.example.playlistsuser.service;

import com.example.playlistsuser.data.repository.content.ContentRepository;
import org.json.JSONObject;

import java.util.List;

public class ContentService {

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public void getAllContents(ContentRepository.ContentCallback callback) {
        contentRepository.getAllContents(new ContentRepository.ContentCallback() {
            @Override
            public void onSuccess(List<JSONObject> contents) {
                callback.onSuccess(contents);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void addContentToPlaylist(int playlistId, int contentId, ContentRepository.ContentAddCallback callback) {
        contentRepository.addContentToPlaylist(playlistId, contentId, new ContentRepository.ContentAddCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void removeContent(int playlistId, int contentId, ContentRepository.ContentRemoveCallback callback) {
        contentRepository.removeContent(playlistId, contentId, new ContentRepository.ContentRemoveCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void getPlaylistContents(int playlistId, ContentRepository.ContentCallback callback) {
        contentRepository.getPlaylistContents(playlistId, callback);
    }
}
