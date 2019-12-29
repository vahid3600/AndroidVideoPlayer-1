package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.model.Audio
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogAdapterListener
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class AudioDialog : VideoPlayerDialog<Audio>() {

    fun showAudioList(audioList: List<Audio>, audioSelectedNumber: Int, listener: VideoPlayerDialogAdapterListener<Audio>) {

        super.showDataList(mapDataList(audioList, audioSelectedNumber), object : VideoPlayerDialogAdapterListener<VideoPlayerDialogModel> {

            override fun onItemClick(t: VideoPlayerDialogModel, position: Int) {
                listener.onItemClick(audioList[position], position)
            }
        })
    }

    private fun mapDataList(audioList: List<Audio>, audioSelectedNumber: Int): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        audioList.map {
            modelList.add(VideoPlayerDialogModel(it.label, it.language, false))
        }

        if (audioSelectedNumber != -1)
        modelList[audioSelectedNumber].isSelected = true

        return modelList
    }
}