package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.database.Subtitle
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class SubtitleDialog : VideoPlayerDialog() {

    fun showSubtitleList(subtitleList: List<Subtitle>, listener: AdapterListener<Subtitle>) {

        super.showDataList(mapDataList(subtitleList), object : AdapterListener<Int> {
            override fun onItemClick(t: Int) {
                listener.onItemClick(subtitleList[t])
                selectedItemNumber = t
            }
        })
    }

    private fun mapDataList(subtitleList: List<Subtitle>): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        subtitleList.map {
            modelList.add(VideoPlayerDialogModel(it.title.toString(), it.subtitleUrl.toString(),false))
        }

        return modelList
    }
}