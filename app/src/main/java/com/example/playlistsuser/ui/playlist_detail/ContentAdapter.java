package com.example.playlistsuser.ui.playlist_detail;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playlistsuser.R;
import com.example.playlistsuser.data.repository.content.ContentRepository;
import com.example.playlistsuser.service.ContentService;
import org.json.JSONObject;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private final List<JSONObject> contentList;
    private final ContentService contentService;
    private final int playlistId;

    public ContentAdapter(List<JSONObject> contentList, ContentService contentService, int playlistId) {
        this.contentList = contentList;
        this.contentService = contentService;
        this.playlistId = playlistId;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        JSONObject content = contentList.get(position);

        holder.title.setText(content.optString("titulo", "Título não disponível"));
        JSONObject creator = content.optJSONObject("criador");
        if (creator != null) {
            holder.creator.setText(creator.optString("nome", "Criador não disponível"));
        } else {
            holder.creator.setText("Criador não disponível");
        }

        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAbsoluteAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            int contentId = contentList.get(currentPosition).optInt("id");
            contentService.removeContent(playlistId, contentId, new ContentRepository.ContentRemoveCallback() {
                @Override
                public void onSuccess() {
                    ((Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                        contentList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, contentList.size());
                        Toast.makeText(holder.itemView.getContext(), "Conteúdo removido com sucesso!", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    ((Activity) holder.itemView.getContext()).runOnUiThread(() ->
                            Toast.makeText(holder.itemView.getContext(), "Erro ao remover conteúdo: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return contentList.size();
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView creator;
        ImageButton btnDelete;

        ContentViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.content_title);
            creator = itemView.findViewById(R.id.content_creator);
            btnDelete = itemView.findViewById(R.id.btn_delete_content);
        }
    }
}
