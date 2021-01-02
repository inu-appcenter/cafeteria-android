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

package com.inu.cafeteria.db

import android.content.Context
import android.preference.PreferenceManager
import android.text.TextUtils
import androidx.core.content.edit

/**
 * Credits to kcochibili.
 * See https://github.com/kcochibili/TinyDB--Android-Shared-Preferences-Turbo
 */
class SharedPreferenceWrapper(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putArrayInt(key: String, arrayInt: Array<Int>) {
        preferences.edit {
            putString(key, TextUtils.join("‚‗‚", arrayInt))
        }
    }

    fun getArrayInt(key: String): Array<Int>? {
        val record = preferences.getString(key, null) ?: return null

        return TextUtils.split(record, "‚‗‚").map { it.toInt() }.toTypedArray()
    }

    fun putString(key: String, value: String?) {
        preferences.edit {
            putString(key, value)
        }
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    fun putInt(key: String, value: Int) {
        preferences.edit {
            putInt(key, value)
        }
    }

    fun getInt(key: String, default: Int = -1): Int {
        return preferences.getInt(key, default)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit {
            putLong(key, value)
        }
    }

    fun getLong(key: String, default: Long = -1): Long {
        return preferences.getLong(key, default)
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit {
            putBoolean(key, value)
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return preferences.getBoolean(key, default)
    }
}