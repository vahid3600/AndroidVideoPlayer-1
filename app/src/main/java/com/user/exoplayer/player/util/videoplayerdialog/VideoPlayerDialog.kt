package com.user.exoplayer.player.util.videoplayerdialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.user.exoplayer.R
import com.user.exoplayer.databinding.FragmentDialogVideoPlayerBinding

open class VideoPlayerDialog<T> : DialogFragment() {

    lateinit var binding: FragmentDialogVideoPlayerBinding

    companion object {

        const val TAG = "CustomDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
                600,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_video_player, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogCancelButton.setOnClickListener { dismiss() }
    }

    fun showDataList(dataList: List<VideoPlayerDialogModel>, listener: VideoPlayerDialogAdapterListener<VideoPlayerDialogModel>) {

        binding.dialogRecyclerView.adapter = VideoPlayerDialogAdapter(dataList, listener)
        binding.dialogRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    fun showTitleScreen(title: String) {
        binding.dialogTitleTextView.text = title
    }

}