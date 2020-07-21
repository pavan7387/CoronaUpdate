package com.example.demo.Util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.demo.R

object AppUtil {

    fun showProgressDialog(activityContext: Context?): Dialog? {
        var dialog: Dialog? = null
        try {
            dialog = Dialog(activityContext!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.progress_dialog_layout)
            dialog.setCancelable(false)
            dialog.show()
        } catch (exception: java.lang.Exception) { //Consume it
            exception.printStackTrace()
        }
        return dialog
    }

}