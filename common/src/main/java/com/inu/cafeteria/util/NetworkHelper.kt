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

package com.inu.cafeteria.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import java.util.concurrent.atomic.AtomicBoolean

class NetworkHelper {

    companion object {

        fun isOnline(connectivityManager: ConnectivityManager): Boolean {
            var result = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        /**
         * Observe network 'CHANGE'.
         * This will not emit onAvailable event when already online.
         */
        fun onNetworkChange(
            connectivityManager: ConnectivityManager,
            onAvailable: () -> Unit,
            onUnavailable: () -> Unit
        ) {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()

            val previousState = AtomicBoolean(isOnline(connectivityManager))

            connectivityManager.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)

                        if (previousState.compareAndSet(false, true)) {
                            onAvailable()
                        }
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()

                        if (previousState.compareAndSet(true, false)) {
                            onUnavailable()
                        }
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        if (previousState.compareAndSet(true, false)) {
                            onUnavailable()
                        }
                    }
                })
        }
    }
}