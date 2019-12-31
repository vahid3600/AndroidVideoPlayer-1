package com.user.exoplayer.player.util.videoplayerdialog;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.user.exoplayer.R;
import com.user.exoplayer.databinding.DialogRecyclerviewItemBinding;

import java.util.List;

public class VideoPlayerDialogAdapter extends RecyclerView.Adapter<VideoPlayerDialogAdapter.AudioViewHolder> {

    private List<VideoPlayerDialogModel> dataList;
    private VideoPlayerDialogAdapterListener listener;

    public VideoPlayerDialogAdapter(List<VideoPlayerDialogModel> dataList, VideoPlayerDialogAdapterListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AudioViewHolder(DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.dialog_recyclerview_item,
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
        DialogRecyclerviewItemBinding binding;

        AudioViewHolder(DialogRecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(int position) {
            binding.setVideoPlayerDialogModel(dataList.get(position));
            binding.itemRadioButton.setOnClickListener(view -> {
                updateDataList(position);
                if (position == 0)
                    listener.onDefaultItemClick();
                else
                    listener.onItemClick(dataList.get(position - 1), position - 1);
            });
            binding.getRoot().setOnClickListener(view -> {
                updateDataList(position);
                if (position == 0)
                    listener.onDefaultItemClick();
                else
                    listener.onItemClick(dataList.get(position - 1), position - 1);
            });
        }
    }

    private void updateDataList(int position) {
        for (VideoPlayerDialogModel model : dataList) {
            model.setSelected(false);
        }
        dataList.get(position).setSelected(true);
    }
}
