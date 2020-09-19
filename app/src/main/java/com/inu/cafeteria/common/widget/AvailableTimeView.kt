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

package com.inu.cafeteria.common.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.inu.cafeteria.R
import com.inu.cafeteria.extension.has

class AvailableTimeView(
    context: Context,
    attributeSet: AttributeSet
) : LinearLayout(context, attributeSet) {

    init {
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.available_time_view, this)
        setAvailableTime(if (isInEditMode) 7 else 0)
    }

    fun setAvailableTime(availableTimes: Int) {
        findViewById<View>(R.id.breakfast).visibility = makeItVisibleOrNotFor(BREAKFAST, availableTimes)
        findViewById<View>(R.id.lunch).visibility = makeItVisibleOrNotFor(LUNCH, availableTimes)
        findViewById<View>(R.id.dinner).visibility = makeItVisibleOrNotFor(DINNER, availableTimes)
    }

    private fun makeItVisibleOrNotFor(time: Int, availableTimes: Int) =
        if (availableTimes.has(time)) View.VISIBLE
        else View.GONE

    companion object {
        private const val BREAKFAST = 0x01
        private const val LUNCH = 0x02
        private const val DINNER = 0x04
    }
}