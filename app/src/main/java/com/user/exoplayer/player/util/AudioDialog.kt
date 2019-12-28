package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.model.Audio
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class AudioDialog : VideoPlayerDialog() {

    fun showAudioList(audioList: List<Audio>, listener: AdapterListener<Audio>) {

        super.showDataList(mapDataList(audioList), object : AdapterListener<Int> {
            override fun onItemClick(t: Int) {
                listener.onItemClick(audioList[t])
                selectedItemNumber = t
            }
        })
    }

    private fun mapDataList(audioList: List<Audio>): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        audioList.map {
            modelList.add(VideoPlayerDialogModel(it.label, it.language, true))
        }

        return modelList
    }
}