package com.example.playlistsuser.data.repository.content;

import org.json.JSONObject;
import java.util.List;

public interface ContentRepository {

    interface ContentCallback {
        void onSuccess(List<JSONObject> contents);
        void onError(String errorMessage);
    }

    interface ContentAddCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    interface ContentRemoveCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    void getAllContents(ContentCallback callback);

    void addContentToPlaylist(int playlistId, int contentId, ContentAddCallback callback);

    void removeContent(int playlistId, int contentId, ContentRemoveCallback callback);

    void getPlaylistContents(int playlistId, ContentRepository.ContentCallback callback);
}
