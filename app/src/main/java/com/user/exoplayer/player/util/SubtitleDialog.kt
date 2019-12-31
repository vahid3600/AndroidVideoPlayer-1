package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.database.Subtitle
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogAdapterListener
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class SubtitleDialog : VideoPlayerDialog<Subtitle>() {

    override var defaultSelectedItemText: String? = "بدون زیرنویس"
    override var selectedItemText: String? = "(در حال پخش)"

    fun showSubtitleList(subtitleList: List<Subtitle>, subtitleSelectedNumber: Int, listener: VideoPlayerDialogAdapterListener<Subtitle>) {

        super.showDataList(mapDataList(subtitleList, subtitleSelectedNumber),
                subtitleSelectedNumber,
                object : VideoPlayerDialogAdapterListener<VideoPlayerDialogModel> {

                    override fun onItemClick(t: VideoPlayerDialogModel, position: Int) {
                        listener.onItemClick(subtitleList[position], position)
                    }

                    override fun onDefaultItemClick() {
                        listener.onDefaultItemClick()
                    }
                })
    }

    private fun mapDataList(subtitleList: List<Subtitle>, subtitleSelectedNumber: Int): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        subtitleList.map {
            modelList.add(VideoPlayerDialogModel(it.title.toString(),false))
        }

        if (subtitleSelectedNumber != -1)
            modelList[subtitleSelectedNumber].isSelected = true
        return modelList
    }
}