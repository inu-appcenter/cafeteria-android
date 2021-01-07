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

package com.inu.cafeteria.common

import android.app.Application
import com.google.mlkit.common.MlKit
import com.inu.cafeteria.common.notification.NotificationChannelManager
import com.inu.cafeteria.injection.myModules
import com.inu.cafeteria.repository.DeviceStatusRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ThisApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startTimber()
        startKoin()
        startDeviceStatusRepository()
        startFirebase()
        initializeNotificationChannel()
        initializeMlKit()
    }

    private fun startTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@ThisApplication)
            modules(myModules)
        }
    }

    private fun startDeviceStatusRepository() {
        val statusRepo: DeviceStatusRepository by inject()
        statusRepo.init()
    }

    private fun startFirebase() {

    }

    private fun initializeNotificationChannel() {
        NotificationChannelManager.addPushNumberNotificationChannel(this)
    }

    private fun initializeMlKit() {
        try {
            /*
             * This is not necessary on every launch.
             * MlKit could have been both initialized or not initialized when we need to use it.
             * Initializing MlKit after is has been initialized will throw an error.
             * Therefore wee need to make sure it is initialized by doing it safely.
             */
            MlKit.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}