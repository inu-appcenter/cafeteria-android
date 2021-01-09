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

import android.net.ConnectivityManager
import com.inu.cafeteria.util.NetworkHelper
import com.inu.cafeteria.util.PublicLiveEvent
import java.util.*
import kotlin.concurrent.schedule

class DeviceStatusRepositoryImpl(
    private val manager: ConnectivityManager
) : DeviceStatusRepository {

    private val networkChangeEvent = PublicLiveEvent<Boolean>()

    override fun init() {
        startObservingNetworkState()
    }

    private fun startObservingNetworkState() {
        val onOnline = { postAfterSomeTime(true) }
        val onOffline = { networkChangeEvent.postValue(false) }

        NetworkHelper.onNetworkChange(manager, onOnline, onOffline)
    }

    private fun postAfterSomeTime(value: Boolean) {
        // Don't know why but after network turned available and isOnline() returns true,
        // the network doesn't work for a while.
        // So we need to wait until it become 'really' available.
        Timer().schedule(500) {
            networkChangeEvent.postValue(value)
        }
    }

    override fun isOnline(): Boolean {
        return NetworkHelper.isOnline(manager)
    }

    override fun networkStateChangeEvent(): PublicLiveEvent<Boolean> = networkChangeEvent
}
