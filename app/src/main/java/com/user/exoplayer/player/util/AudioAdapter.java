package com.user.exoplayer.player.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.user.exoplayer.R;
import com.user.exoplayer.player.data.model.Audio;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private List<Audio> audioList;
    private VideoPlayer player;

    public AudioAdapter(List<Audio> audioList, VideoPlayer player) {
        this.audioList = audioList;
        this.player = player;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AudioViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.audio_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder audioViewHolder, int i) {
        audioViewHolder.onBind(audioList.get(i));
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView audioName;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            this.audioName = itemView.findViewById(R.id.audio_text_view);
        }

        public void onBind(Audio audio) {
            audioName.setText(audio.getLabel());
            itemView.setOnClickListener(view -> {

                player.setSelectedAudio(audio.getLanguage());
            });
        }
    }
}
