package com.example.playlistsuser.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playlistsuser.R;
import com.example.playlistsuser.data.repository.playlist.PlaylistRepository;
import com.example.playlistsuser.model.Playlist;
import com.example.playlistsuser.data.repository.playlist.PlaylistRepositoryImpl;
import com.example.playlistsuser.data.repository.SessionManager;
import com.example.playlistsuser.service.PlaylistService;
import com.example.playlistsuser.ui.playlist_detail.PlaylistDetailActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;
    private PlaylistService playlistService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SessionManager sessionManager = new SessionManager(this);
        String authToken = sessionManager.getAuthToken();

        playlistService = new PlaylistService(new PlaylistRepositoryImpl(this));

        playlistList = new ArrayList<>();
        RecyclerView playlistsRecyclerView = findViewById(R.id.playlists_recycler_view);
        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        playlistAdapter = new PlaylistAdapter(playlistList, playlistId -> {
            Intent intent = new Intent(HomeActivity.this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", playlistId);
            startActivity(intent);
        });


        playlistsRecyclerView.setAdapter(playlistAdapter);

        ImageButton fabAddPlaylist = findViewById(R.id.fab_add_playlist);
        fabAddPlaylist.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nova Playlist");

            final EditText input = new EditText(this);
            input.setHint("Digite o nome da playlist");
            builder.setView(input);

            builder.setPositiveButton("Criar", (dialog, which) -> {
                String playlistName = input.getText().toString().trim();
                if (!playlistName.isEmpty()) {
                    createPlaylist(playlistName);
                } else {
                    Toast.makeText(this, "O nome da playlist não pode estar vazio.", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        loadPlaylists();
    }

    private void loadPlaylists() {
        playlistService.fetchPlaylists(new PlaylistRepository.PlaylistsCallback() {
            @Override
            public void onSuccess(List<Playlist> playlists) {
                runOnUiThread(() -> {
                    playlistList.clear();
                    playlistList.addAll(playlists);

                    playlistAdapter.notifyItemRangeInserted(0, playlists.size());
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(HomeActivity.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void createPlaylist(String playlistName) {
        playlistService.createPlaylist(playlistName, new PlaylistRepository.PlaylistCreateCallback() {
            @Override
            public void onSuccess(Playlist newPlaylist) {
                runOnUiThread(() -> {
                    playlistList.add(newPlaylist);
                    playlistAdapter.notifyItemInserted(playlistList.size() - 1);
                    Toast.makeText(HomeActivity.this, "Playlist criada com sucesso!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
