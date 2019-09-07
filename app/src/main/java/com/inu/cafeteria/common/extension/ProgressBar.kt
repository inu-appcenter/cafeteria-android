package com.inu.cafeteria.common.extension

import android.content.res.ColorStateList
import android.widget.ProgressBar

fun ProgressBar.setTint(color: Int) {
    indeterminateTintList = ColorStateList.valueOf(color)
    progressTintList = ColorStateList.valueOf(color)
}

