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

package com.inu.cafeteria.feature.support.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Notice
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.usecase.GetAllNotices
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class NoticeViewModel : BaseViewModel() {

    private val getAllNotices: GetAllNotices by inject()

    private val _notices = MutableLiveData<List<NoticeView>>()
    val notices: LiveData<List<NoticeView>> = _notices

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    fun load() {
        if (handleIfOffline()) {
            Timber.w("Offline! Fetch canceled.")
            return
        }

        _loading.value = true

        getAllNotices(Unit) {
            it.onSuccess(::handleNotices).onError(::handleFailure).finally { _loading.value = false }
        }
    }

    private fun handleNotices(notices: List<Notice>) {
        val noticeViews = notices.map {
            NoticeView(
                title = it.title,
                body = it.body,
                date = dateString(it.createdAt)
            )
        }

        _notices.value = noticeViews
    }

    private fun dateString(timestamp: Long): String {
        return Date(timestamp).format("yyyy/MM/dd")
    }
}