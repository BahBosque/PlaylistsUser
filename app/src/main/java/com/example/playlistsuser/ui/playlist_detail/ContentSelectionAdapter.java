package com.example.playlistsuser.ui.playlist_detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playlistsuser.R;
import org.json.JSONObject;
import java.util.List;

public class ContentSelectionAdapter extends RecyclerView.Adapter<ContentSelectionAdapter.ContentViewHolder> {

    private final List<JSONObject> contentList;
    private final OnContentSelectedListener listener;

    public interface OnContentSelectedListener {
        void onContentSelected(JSONObject content);
    }

    public ContentSelectionAdapter(List<JSONObject> contentList, OnContentSelectedListener listener) {
        this.contentList = contentList;
        this.listener = listener;
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

        holder.btnAdd.setImageResource(android.R.drawable.ic_input_add);
        holder.btnAdd.setOnClickListener(v -> listener.onContentSelected(content));
    }

    @Override
    public int getItemCount() {
        return contentList != null ? contentList.size() : 0;
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView creator;
        ImageButton btnAdd;

        ContentViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.content_title);
            creator = itemView.findViewById(R.id.content_creator);
            btnAdd = itemView.findViewById(R.id.btn_add_content);

            if (title == null || creator == null || btnAdd == null) {
                throw new IllegalStateException("Os elementos do layout não foram encontrados. Verifique o arquivo XML.");
            }
        }
    }
}
