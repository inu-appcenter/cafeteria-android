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

package com.inu.cafeteria.common.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.inu.cafeteria.repository.ExternalCredentialsRepository
import org.koin.android.ext.android.inject
import timber.log.Timber

class CafeteriaFirebaseMessagingService : FirebaseMessagingService() {

    private val externalCredentialsRepo: ExternalCredentialsRepository by inject()

    override fun onNewToken(token: String) {
        /**
         * This will happen when:
         * - The app deletes instance ID
         * - The app is restored on a new device
         * - The user uninstall/reinstall the app
         * - The user clears app data
         * (Not when app is updated!)
         *
         * Even after a token changed,
         * the old token will still work for short period.
         */
        Timber.i("New token published!")

        externalCredentialsRepo.saveFirebaseToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        /**
         * Notification message in 'notification',
         * The pushed number in 'data'
         */

        // Notify this app-wide
        sendBroadcast(Intent().apply {
            action = ACTION_PUSH_NUMBER_NOTIFICATION
            putExtra("message", message)
        })
    }

    companion object {
        const val ACTION_PUSH_NUMBER_NOTIFICATION = "com.inu.cafeteria.push_number_notification"
    }
}