package org.inu.cafeteria.common.extension

import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.ColorRes


fun TextView.setBold(bold: Boolean) {
    // TODO default value
    this.setTypeface(this.typeface, if (bold) Typeface.BOLD else Typeface.NORMAL)
}

fun TextView.setTextColorRes(@ColorRes colorResId: Int) {
    resources.getColor(colorResId, null)
}
