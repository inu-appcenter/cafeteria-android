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

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.inu.cafeteria.extension.withNonNull

fun AppCompatActivity.setSupportActionBar(toolbar: Toolbar, title: Boolean = false, upButton: Boolean = false) {
    setSupportActionBar(toolbar)

    withNonNull(supportActionBar) {
        setDisplayShowTitleEnabled(title)
        setDisplayHomeAsUpEnabled(upButton)
    }
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Activity.dismissKeyboard() {
    window.currentFocus?.let { focus ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focus.windowToken, 0)

        focus.clearFocus()
    }
}

/**
 * @param brightness from 0 to 1
 */
fun Activity.setBrightness(brightness: Float) {
    val params = window.attributes.apply {
        screenBrightness = brightness
    }
    window.attributes = params
}

fun Activity.resetBrightness() {
    setBrightness(-1f)
}

/**
 * @return from 0 to 1
 */
fun Activity.getBrightness(): Float {
    return window.attributes.screenBrightness
}