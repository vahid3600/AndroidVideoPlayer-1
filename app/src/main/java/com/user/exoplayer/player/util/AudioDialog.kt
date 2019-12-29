package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.model.Audio
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogAdapterListener
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class AudioDialog : VideoPlayerDialog<Audio>() {

    fun showAudioList(audioList: List<Audio>, listener: VideoPlayerDialogAdapterListener<Audio>) {

        super.showDataList(mapDataList(audioList), object : VideoPlayerDialogAdapterListener<VideoPlayerDialogModel> {

            override fun onItemClick(t: VideoPlayerDialogModel, position: Int) {
                listener.onItemClick(audioList[position], position)
            }
        })
    }

    private fun mapDataList(audioList: List<Audio>): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        audioList.map {
            modelList.add(VideoPlayerDialogModel(it.label, it.language, it.isSelected))
        }

        return modelList
    }
}