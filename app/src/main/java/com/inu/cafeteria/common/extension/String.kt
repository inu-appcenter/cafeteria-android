/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

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