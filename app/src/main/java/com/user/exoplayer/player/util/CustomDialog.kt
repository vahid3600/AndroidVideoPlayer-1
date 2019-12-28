package com.user.exoplayer.player.util

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.user.exoplayer.R
import kotlinx.android.synthetic.main.fragment_dialog_custome.*

open class CustomDialog : DialogFragment() {

    companion object {

        const val TAG = "CustomDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.CustomDialog)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
                600,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_custome,container, false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog_cancel_button?.setOnClickListener { dismiss() }
    }

    fun showDataList(dataList: List<CustomDialogModel>, listener: AdapterListener<Int>) {

        dialog_recycler_view.adapter = CustomDialogAdapter(dataList, listener)
        dialog_recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    fun showTitleScreen(title: String){
        dialog_title_text_view.text = title
    }

}