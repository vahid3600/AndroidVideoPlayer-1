package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.database.Subtitle
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogAdapterListener
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class SubtitleDialog : VideoPlayerDialog<Subtitle>() {

    fun showSubtitleList(subtitleList: List<Subtitle>, listener: VideoPlayerDialogAdapterListener<Subtitle>) {

        super.showDataList(mapDataList(subtitleList), object : VideoPlayerDialogAdapterListener<VideoPlayerDialogModel> {

            override fun onItemClick(t: VideoPlayerDialogModel, position: Int) {
                listener.onItemClick(subtitleList[position],position)
            }
        })
    }

    private fun mapDataList(subtitleList: List<Subtitle>): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        subtitleList.map {
            modelList.add(VideoPlayerDialogModel(it.title.toString(), it.subtitleUrl.toString(), it.isSelected))
        }

        return modelList
    }
}