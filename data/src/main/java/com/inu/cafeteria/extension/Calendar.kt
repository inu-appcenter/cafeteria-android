/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.getFormatedDate(): String {
    return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(this.time)
}