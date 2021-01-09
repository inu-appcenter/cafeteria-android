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

package com.inu.cafeteria.common.extension

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.inu.cafeteria.extension.tryOrNull

fun Context.getColorCompat(colorRes: Int): Int {
    //return black as a default color, in case an invalid color ID was passed in
    return tryOrNull { ContextCompat.getColor(this, colorRes) } ?: Color.BLACK
}

/**
 * Tries to resolve a resource id from the current theme, based on the [attributeId]
 */

fun Context.resolveThemeAttribute(attributeId: Int, default: Int = 0): Int {
    val outValue = TypedValue()
    val wasResolved = theme.resolveAttribute(attributeId, outValue, true)

    return if (wasResolved) outValue.resourceId else default
}

fun Context.resolveThemeColor(attributeId: Int, default: Int = 0): Int {
    val outValue = TypedValue()
    val wasResolved = theme.resolveAttribute(attributeId, outValue, true)

    return if (wasResolved) getColorCompat(outValue.resourceId) else default
}

fun Context.resolveColor(@ColorRes res: Int): Int {
    return this.resources.getColor(res, theme)
}

fun Context.registerReceiver(action: String, onReceive: (intent: Intent?) -> Unit): BroadcastReceiver {
    val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onReceive(intent)
        }
    }

    registerReceiver(receiver, IntentFilter(action))

    return receiver
}