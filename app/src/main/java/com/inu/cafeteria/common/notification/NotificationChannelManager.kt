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

package com.inu.cafeteria.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import com.inu.cafeteria.R

object NotificationChannelManager {

    fun addPushNumberNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = buildChannel(context)

            registerChannel(context, channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildChannel(context: Context): NotificationChannel {
        // Create the NotificationChannel
        val channelId = context.getString(R.string.push_number_notification_channel_id)
        val name = context.getString(R.string.push_number_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_HIGH // We need to show it big and noisy!

        return NotificationChannel(channelId, name, importance).apply {
            description = context.getString(R.string.push_number_notification_channel_description)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerChannel(context: Context, channel: NotificationChannel) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}