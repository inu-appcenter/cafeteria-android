/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.common.extension

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.provider.Settings
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
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

fun Context.makeToast(@StringRes res: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, res, duration).show()
}

fun Context.makeToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Activity.dismissKeyboard() {
    window.currentFocus?.let { focus ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focus.windowToken, 0)

        focus.clearFocus()
    }
}

fun Context.registerReceiver(action: String, onReceive: (intent: Intent?) -> Unit) {
    registerReceiver(object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onReceive(intent)
        }
    }, IntentFilter(action))
}

/**
 * @param brightness from 0 to 255
 */
fun Context.setBrightness(brightness: Int) {
    if (Settings.System.canWrite(this)) {
        val resolver = contentResolver
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
    }
}

/**
 * @return from 0 th 255
 */
fun Context.getBrightness(): Int {
    val resolver = contentResolver
    return Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
}