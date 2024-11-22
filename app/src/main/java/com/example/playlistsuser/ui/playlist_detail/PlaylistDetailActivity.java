package com.example.playlistsuser.ui.playlist_detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playlistsuser.R;
import com.example.playlistsuser.data.repository.SessionManager;
import com.example.playlistsuser.data.repository.content.ContentRepository;
import com.example.playlistsuser.data.repository.content.ContentRepositoryImpl;
import com.example.playlistsuser.model.Playlist;
import com.example.playlistsuser.service.ContentService;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailActivity extends AppCompatActivity {

    private RecyclerView contentsRecyclerView;
    private ContentAdapter contentAdapter;
    private List<JSONObject> contentList;
    private ContentService contentService;
    private int playlistId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);

        playlistId = getIntent().getIntExtra("playlistId", -1);
        if (playlistId == -1) {
            Toast.makeText(this, "ID da playlist não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SessionManager sessionManager = new SessionManager(this);
        ContentRepositoryImpl contentRepository = new ContentRepositoryImpl(sessionManager, new okhttp3.OkHttpClient());
        contentService = new ContentService(contentRepository);

        TextView playlistName = findViewById(R.id.playlist_name);
        contentsRecyclerView = findViewById(R.id.contents_recycler_view);
        ImageButton btnBack = findViewById(R.id.btn_back);
        ImageButton btnAddContent = findViewById(R.id.btn_add_content);

        contentList = new ArrayList<>();
        contentAdapter = new ContentAdapter(contentList, contentService, playlistId);

        contentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentsRecyclerView.setAdapter(contentAdapter);

        btnBack.setOnClickListener(v -> finish());

        btnAddContent.setOnClickListener(v -> showContentSelectionDialog());

        loadPlaylistContents(playlistId, playlistName);
    }

    private void loadPlaylistContents(int playlistId, TextView playlistName) {
        contentService.getPlaylistContents(playlistId, new ContentRepository.ContentCallback() {
            @Override
            public void onSuccess(List<JSONObject> contents) {
                runOnUiThread(() -> {
                    int oldSize = contentList.size();
                    contentList.clear();
                    contentAdapter.notifyItemRangeRemoved(0, oldSize);
                    contentList.addAll(contents);
                    contentAdapter.notifyItemRangeInserted(0, contents.size());

                    playlistName.setText("Playlist ID: " + playlistId);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(PlaylistDetailActivity.this, "Erro ao carregar conteúdos da playlist: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }


    private void showContentSelectionDialog() {
        contentService.getAllContents(new ContentRepository.ContentCallback() {
            @Override
            public void onSuccess(List<JSONObject> allContents) {
                runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistDetailActivity.this);
                    builder.setTitle("Selecione um Conteúdo");

                    RecyclerView recyclerView = new RecyclerView(PlaylistDetailActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PlaylistDetailActivity.this));
                    ContentSelectionAdapter adapter = new ContentSelectionAdapter(allContents, selectedContent -> {
                        addContentToPlaylist(selectedContent.optInt("id"));
                    });
                    recyclerView.setAdapter(adapter);
                    builder.setView(recyclerView);

                    builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                    builder.show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(PlaylistDetailActivity.this, "Erro ao obter conteúdos: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addContentToPlaylist(int contentId) {
        contentService.addContentToPlaylist(playlistId, contentId, new ContentRepository.ContentAddCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(PlaylistDetailActivity.this, "Conteúdo adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                    loadPlaylistContents(playlistId, findViewById(R.id.playlist_name));
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(PlaylistDetailActivity.this, "Erro ao adicionar conteúdo: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
