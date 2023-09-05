package com.developer.cory.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.developer.cory.R
import com.developer.cory.databinding.CustomLoadingDialogBinding

class CustomDialog(private val context: Context) {

    private lateinit var dialog: Dialog

    fun dialogBasic(title:String?){
        dialog = Dialog(context,R.style.CustomAlert)
        dialog.setContentView(R.layout.custom_loading_dialog)

        val textView = dialog.findViewById<TextView>(R.id.title)
        textView.text = title?:"Đang xử lý ...."

        dialog.setCancelable(false)
        dialog.show()
    }



    fun closeDialog(){
        dialog.dismiss()
    }
}