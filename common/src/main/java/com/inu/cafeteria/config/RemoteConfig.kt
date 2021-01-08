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

package com.inu.cafeteria.config

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.internal.DefaultsXmlParser
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.inu.cafeteria.common.R
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Configuration priority:
 *  1: Server
 *  2: Local defaults (RemoteConfig disk cache)
 *  3: Local fallback (R.xml.config_defaults)
 */
object RemoteConfig : KoinComponent {

    private val context: Context by inject()
    private val fallback: Map<String, String> = DefaultsXmlParser.getDefaultsFromXml(context, R.xml.config_defaults)

    private var configReady = false

    private val remoteConfig = Firebase.remoteConfig.apply {
        setDefaultsAsync(fallback).addOnCompleteListener { configReady = true }

        safeFetch()
    }

    private fun safeFetch(): Task<Boolean>? {
        return try {
            remoteConfig.fetchAndActivate()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getBoolean(key: String) = if (configReady) remoteConfig.getBoolean(key) else fallback[key].toBoolean()
    fun getString(key: String) = if (configReady) remoteConfig.getString(key) else fallback[key] ?: ""
}