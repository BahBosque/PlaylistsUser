package com.example.playlistsuser.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playlistsuser.R;
import com.example.playlistsuser.model.Playlist;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final List<Playlist> playlists;
    private final OnPlaylistClickListener listener;

    public interface OnPlaylistClickListener {
        void onPlaylistClick(int playlistId);
    }

    public PlaylistAdapter(List<Playlist> playlists, OnPlaylistClickListener listener) {
        this.playlists = playlists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.playlistTitle.setText(playlist.getTitle());

        holder.itemView.setTag(playlist.getId());

        holder.itemView.setOnClickListener(v -> {
            listener.onPlaylistClick(playlist.getId());
        });
    }


    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistTitle;

        PlaylistViewHolder(View itemView) {
            super(itemView);
            playlistTitle = itemView.findViewById(R.id.playlist_title);
        }
    }
}
