/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.model.scheme

/**
 * Scheme for notice.
 */
data class Notice(
    val all: PlatformNotice,
    val android: PlatformNotice
) {
    data class PlatformNotice(val id: Long, val title: String, val message: String)

    companion object {
        fun emptyNotice(): Notice {
            return Notice(
                PlatformNotice(-1L, "", ""),
                PlatformNotice(-1L, "", "")
            )
        }
    }
}