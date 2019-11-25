/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.Notice

abstract class NoticeRepository : Repository() {
    abstract fun getNotice(callback: DataCallback<Notice>)

    abstract fun dismissNotice(type: DeviceType, id: Long)
    abstract fun getDismissedNoticeId(type: DeviceType): Long?

    enum class DeviceType(val key: String) {
        All("dismissedAllNoticeId"),
        Android("dismissedAndroidNoticeId")
    }
}