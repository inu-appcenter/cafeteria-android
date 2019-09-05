package org.inu.cafeteria.common.extension

import java.net.URL

fun String.addUrl(spec: String?): String {
    return URL(URL(this), spec).toString()
}