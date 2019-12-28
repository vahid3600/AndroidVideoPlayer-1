package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.database.Subtitle

object SubtitleDialog : CustomDialog() {

    fun showSubtitleList(subtitleList: List<Subtitle>, listener: AdapterListener<Subtitle>) {

        super.showDataList(mapDataList(subtitleList), object :AdapterListener<Int>{
            override fun onItemClick(t: Int) {
                listener.onItemClick(subtitleList[t])
            }
        })
    }

    private fun mapDataList(subtitleList: List<Subtitle>): List<CustomDialogModel> {

        val modelList = ArrayList<CustomDialogModel>()

        subtitleList.map {
            modelList.add(CustomDialogModel(it.title.toString(), it.subtitleUrl.toString()))
        }

        return modelList
    }
}