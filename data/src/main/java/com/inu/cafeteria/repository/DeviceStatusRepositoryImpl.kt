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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.util.NetworkHelper

class DeviceStatusRepositoryImpl(
    private val manager: ConnectivityManager
) : DeviceStatusRepository {

    private val online = MutableLiveData<Boolean>(null)

    override fun init() {
        applyCurrentNetworkStateToLiveData()
        startObservingNetworkState()
    }

    private fun applyCurrentNetworkStateToLiveData() {
        online.value = isOnline()
    }

    private fun startObservingNetworkState() {
        val onOnline = { online.postValue(true) }
        val onOffline = { online.postValue(false) }

        NetworkHelper.onNetworkChange(manager, onOnline, onOffline)
    }

    override fun isOnline(): Boolean {
        return NetworkHelper.isOnline(manager)
    }

    override fun isOnlineEvent(): LiveData<Boolean> = online
}