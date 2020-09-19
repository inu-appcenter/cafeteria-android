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
import com.inu.cafeteria.common.BuildConfig
import com.inu.cafeteria.extension.onResult
import com.inu.cafeteria.model.Cache
import com.inu.cafeteria.model.scheme.Version
import com.inu.cafeteria.service.CafeteriaNetworkService
import timber.log.Timber

class VersionRepositoryImpl(
    private val context: Context,
    private val networkService: CafeteriaNetworkService
) : VersionRepository() {

    private val pref = context.getSharedPreferences(
        PREFERENCE_VERSION_INFO,
        Activity.MODE_PRIVATE
    )

    private val versionCache = Cache<Version>()

    override fun dismissVersion(version: String) {
        pref.edit(true) {
            putString(KEY_DISMISSED_VERSION, version)
        }
    }

    override fun getDismissedVersion(): String? {
        return pref.getString(KEY_DISMISSED_VERSION, EMPTY)
    }

    override fun getCurrentVersion(): String {
        // BuildConfig from module common.
        return BuildConfig.VERSION_NAME
    }

    override fun getLatestVersion(callback: Repository.DataCallback<Version>) {
        if (versionCache.isValid) {
            versionCache.get()?.let {
                callback.onSuccess(it)
                Timber.i("Got latest version info from cache!")
                return
            }
        }

        networkService.getVersionResult().onResult(
            async = callback.async,
            onSuccess = {
                callback.onSuccess(it)
                versionCache.set(it)
            },
            onFail = callback.onFail
        )
    }


    companion object {
        private const val PREFERENCE_VERSION_INFO = "versionInfo"
        private const val KEY_DISMISSED_VERSION = "dismissedVersion"
        private const val EMPTY = ""
    }
}