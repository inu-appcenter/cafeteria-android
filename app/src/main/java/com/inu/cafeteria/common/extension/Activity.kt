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

/**
 * @return from 0 to 1
 */
fun Activity.getBrightness(): Float {
    return window.attributes.screenBrightness
}