package com.inu.cafeteria.common.extension

import android.text.Html
import android.text.Spanned
import java.net.URL

fun String.addUrl(spec: String?): String {
    return URL(URL(this), spec).toString()
}

fun String.partialBold(part: String): Spanned {
    return Html.fromHtml(
         replace(part, "<b>$part</b>").replace("\n", "<br>"), 0
    )
}