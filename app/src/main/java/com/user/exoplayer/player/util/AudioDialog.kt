package com.user.exoplayer.player.util

import com.user.exoplayer.player.data.model.Audio

object AudioDialog : CustomDialog() {

    fun showAudioList(audioList: List<Audio>, listener: AdapterListener<Audio>) {

        super.showDataList(mapDataList(audioList), object :AdapterListener<Int>{
            override fun onItemClick(t: Int) {
                listener.onItemClick(audioList[t])
            }
        })
    }

    private fun mapDataList(audioList: List<Audio>): List<CustomDialogModel> {

        val modelList = ArrayList<CustomDialogModel>()

        audioList.map {
            modelList.add(CustomDialogModel(it.label, it.language))
        }

        return modelList
    }
}