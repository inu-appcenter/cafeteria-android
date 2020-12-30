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

import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.entities.Notice
import com.inu.cafeteria.exception.NullBodyException
import com.inu.cafeteria.exception.ResourceNotFoundException
import com.inu.cafeteria.extension.getOrThrow
import com.inu.cafeteria.retrofit.CafeteriaNetworkService
import com.inu.cafeteria.retrofit.scheme.NoticeResult

class NoticeRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val db: SharedPreferenceWrapper
) : NoticeRepository {

    override fun getNewNotice(os: String, version: String): Notice? {
        val notice = try {
            getLatestNotice(os, version)
        } catch (exception: ResourceNotFoundException) {
            return null
        }

        val lastSeenNoticeId = this.db.getInt(KEY_LAST_SEEN_NOTICE_ID)

        // Return only when this notice is first to user.
        return if (notice.id > lastSeenNoticeId) {
            notice
        } else {
            null
        }
    }

    private fun getLatestNotice(os: String, version: String): Notice {
        val latestNoticeFromServer = this.networkService
            .getLatestNotice(os, version)
            .getOrThrow() ?: throw NullBodyException("Body should not be empty!")

        return parseNotice(latestNoticeFromServer)
    }

    override fun markNoticeRead(notice: Notice) {
        this.db.putInt(KEY_LAST_SEEN_NOTICE_ID, notice.id)
    }

    override fun getAllNotices(os: String, version: String): List<Notice> {
        val allNoticesFromServer = this.networkService
            .getAllNotices(os, version)
            .getOrThrow() ?: throw NullBodyException("Body should not be empty!")

        return allNoticesFromServer.map { parseNotice(it) }
    }

    private fun parseNotice(raw: NoticeResult) = Notice(
        raw.id, raw.title, raw.body, raw.createdAt
    )

    companion object {
        private const val KEY_LAST_SEEN_NOTICE_ID = "key_last_seen_notice_id"
    }
}