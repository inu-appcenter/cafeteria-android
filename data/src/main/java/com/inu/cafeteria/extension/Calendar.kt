package com.inu.cafeteria.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.getFormatedDate(): String {
    return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(this.time)
}