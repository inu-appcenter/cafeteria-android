/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import com.inu.cafeteria.extension.onResult
import com.inu.cafeteria.util.Cache
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

    override fun getNotice(callback: Repository.DataCallback<Notice>) {
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