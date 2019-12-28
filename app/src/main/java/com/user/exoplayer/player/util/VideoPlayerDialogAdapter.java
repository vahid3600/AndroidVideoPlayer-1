package com.user.exoplayer.player.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.user.exoplayer.R;

import java.util.List;

public class VideoPlayerDialogAdapter extends RecyclerView.Adapter<VideoPlayerDialogAdapter.AudioViewHolder> {

    private List<VideoPlayerDialogModel> dataList;
    private AdapterListener listener;

    public VideoPlayerDialogAdapter(List<VideoPlayerDialogModel> dataList, AdapterListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AudioViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dialog_recyclerview_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder audioViewHolder, int i) {
        audioViewHolder.onBind(i);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleTextView = itemView.findViewById(R.id.item_text_view);
        }

        void onBind(int position) {
            titleTextView.setText(dataList.get(position).getTitle());
            itemView.setOnClickListener(view -> {

                listener.onItemClick(position);
            });
        }
    }
}
