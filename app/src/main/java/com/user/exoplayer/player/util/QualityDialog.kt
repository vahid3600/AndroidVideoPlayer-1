package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.model.Quality
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogAdapterListener
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogModel

class QualityDialog : VideoPlayerDialog<Quality>() {

    override var defaultSelectedItemText: String? = "خودکار"
    override var selectedItemText: String? = "(در حال پخش)"

    fun showQualityList(qualityList: List<Quality>, qualitySelectedNumber: Int, listener: VideoPlayerDialogAdapterListener<Quality>) {

        super.showDataList(mapDataList(qualityList),
                qualitySelectedNumber,
                object : VideoPlayerDialogAdapterListener<VideoPlayerDialogModel> {

                    override fun onItemClick(t: VideoPlayerDialogModel, position: Int) {
                        listener.onItemClick(qualityList[position], position)
                    }

                    override fun onDefaultItemClick() {
                        listener.onDefaultItemClick()
                    }
                })
    }

    private fun mapDataList(qualityList: List<Quality>): List<VideoPlayerDialogModel> {

        val modelList = ArrayList<VideoPlayerDialogModel>()

        qualityList.map {
            modelList.add(VideoPlayerDialogModel(it.bitrate, it.isSelected))
        }

        return modelList
    }
}