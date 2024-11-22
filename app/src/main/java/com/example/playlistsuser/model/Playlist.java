package com.example.playlistsuser.model;

import org.json.JSONArray;

public class Playlist {
    private String title;
    private JSONArray content;
    private final int id;

    public Playlist(int id, String title, JSONArray content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public JSONArray getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(JSONArray content) {
        this.content = content;
    }
}
