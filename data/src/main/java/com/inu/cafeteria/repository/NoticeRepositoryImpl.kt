/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import com.inu.cafeteria.extension.onResult
import com.inu.cafeteria.model.Cache
import com.inu.cafeteria.model.scheme.Notice
import com.inu.cafeteria.service.CafeteriaNetworkService
import timber.log.Timber

class NoticeRepositoryImpl(
    private val context: Context,
    private val networkService: CafeteriaNetworkService
) : NoticeRepository() {

    private val pref = context.getSharedPreferences(
        PREFERENCE_NOTICE_INFO,
        Activity.MODE_PRIVATE
    )

    private val noticeCache = Cache<Notice>()

    override fun getNotice(callback: DataCallback<Notice>) {
        if (noticeCache.isValid) {
            noticeCache.get()?.let {
                callback.onSuccess(it)
                Timber.i("Got all notice from cache!")
                return
            }
        }

        networkService.getNotice().onResult(
            async = callback.async,
            onSuccess = {
                callback.onSuccess(it)
                noticeCache.set(it)
            },
            onFail = callback.onFail
        )
    }

    override fun dismissNotice(type: DeviceType, id: Long) {
        pref.edit(true) {
            putLong(type.key, id)
        }
    }

    override fun getDismissedNoticeId(type: DeviceType): Long? {
        return pref.getLong(type.key, EMPTY).takeIf { it != EMPTY }
    }

    companion object {
        private const val PREFERENCE_NOTICE_INFO = "noticeInfo"
        private const val EMPTY = -1L
    }
}