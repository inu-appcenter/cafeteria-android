package org.inu.cafeteria.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle

import android.view.WindowManager
import org.inu.cafeteria.R

class ThemedDialog(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window!!.attributes = layoutParams

        setContentView(R.layout.)

    }
}