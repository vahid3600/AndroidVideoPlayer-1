package com.user.exoplayer.player.util.videoplayerdialog

interface VideoPlayerDialogAdapterListener<T> {

    fun onItemClick(t: T, position: Int)
    fun onDefaultItemClick()
}