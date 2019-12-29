package com.user.exoplayer.player.util.videoplayerdialog

import android.content.res.Resources
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider

class VideoPlayerDialogUtil {

    private val BITRATE_1080P = 2800000
    private val BITRATE_720P = 1600000
    private val BITRATE_480P = 700000
    private val BITRATE_360P = 530000
    private val BITRATE_240P = 400000
    private val BITRATE_160P = 300000

    fun buildBitrateString(format: Format, resources: Resources): String {
        val bitrate = format.bitrate
        if (bitrate == Format.NO_VALUE) {
            return DefaultTrackNameProvider(resources).getTrackName(format)
        }
        if (bitrate <= BITRATE_160P) {
            return " 160P"
        }
        if (bitrate <= BITRATE_240P) {
            return " 240P"
        }
        if (bitrate <= BITRATE_360P) {
            return " 360P"
        }
        if (bitrate <= BITRATE_480P) {
            return " 480P"
        }
        if (bitrate <= BITRATE_720P) {
            return " 720P"
        }
        return if (bitrate <= BITRATE_1080P) {
            " 1080P"
        } else DefaultTrackNameProvider(resources).getTrackName(format)
    }
}