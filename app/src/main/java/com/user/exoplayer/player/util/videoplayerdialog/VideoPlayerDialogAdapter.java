package com.user.exoplayer.player.util.videoplayerdialog;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.user.exoplayer.R;
import com.user.exoplayer.databinding.DialogRecyclerviewItemBinding;
import com.user.exoplayer.player.util.AdapterListener;

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
        return new AudioViewHolder(DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),R.layout.dialog_recyclerview_item,
                        viewGroup, false));
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
        DialogRecyclerviewItemBinding binding;

        AudioViewHolder(DialogRecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(int position) {
            binding.setVideoPlayerDialogModel(dataList.get(position));
            itemView.setOnClickListener(view -> {

                listener.onItemClick(position);
            });
        }
    }
}
