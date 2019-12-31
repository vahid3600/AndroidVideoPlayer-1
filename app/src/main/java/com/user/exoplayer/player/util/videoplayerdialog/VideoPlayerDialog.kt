package com.user.exoplayer.player.util.videoplayerdialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.user.exoplayer.R
import com.user.exoplayer.databinding.FragmentDialogVideoPlayerBinding

abstract class VideoPlayerDialog<T> : DialogFragment() {

    lateinit var binding: FragmentDialogVideoPlayerBinding
    abstract var defaultSelectedItemText: String?
    abstract var selectedItemText: String?

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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setGravity(Gravity.CENTER)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_video_player, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogCancelButton.setOnClickListener { dismiss() }
    }

    fun showDataList(dataList: List<VideoPlayerDialogModel>,
                     selectedItemPosition: Int,
                     listener: VideoPlayerDialogAdapterListener<VideoPlayerDialogModel>) {

        dataList.map {

            for (i in dataList.indices) {
                if (dataList[i].isSelected && !dataList[i].title.contains(selectedItemText.toString())) {
                    dataList[i].title = dataList[i].title + " " + selectedItemText
                    if (selectedItemPosition == -1)
                        dataList[i].isSelected = false
                }
            }
        }
        val videoPlayerDialogModelList = ArrayList<VideoPlayerDialogModel>()
        if (defaultSelectedItemText != null)
            videoPlayerDialogModelList.add(VideoPlayerDialogModel(defaultSelectedItemText.toString(), !checkDataListIsSelected(dataList)))
        videoPlayerDialogModelList.addAll(dataList)
        binding.dialogRecyclerView.adapter = VideoPlayerDialogAdapter(videoPlayerDialogModelList, listener)
        binding.dialogRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun checkDataListIsSelected(dataList: List<VideoPlayerDialogModel>): Boolean {
        for (model in dataList)
            if (model.isSelected)
                return true
        return false
    }

    fun showTitleScreen(title: String) {
        binding.dialogTitleTextView.text = title
    }

}