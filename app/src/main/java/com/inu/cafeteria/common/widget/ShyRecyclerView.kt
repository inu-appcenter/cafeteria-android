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
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.tan

/**
 * This RecyclerView allows vertical scroll only when scroll angle is steep (over 60 degree).
 * Otherwise, the scroll event will not be consumed.
 */
class ShyRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private val gestureDetector = GestureDetector(context, YScrollDetector())

    // Pass scroll event to child view if scroll angle lower than this.
    private val verticalScrollThresholdAngleDegree = 70.0
    private val diffRatioThreshold = tan(Math.toRadians(verticalScrollThresholdAngleDegree))

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e) && e != null && gestureDetector.onTouchEvent(e)
    }

    inner class YScrollDetector : SimpleOnGestureListener() {
        // Return false if scroll angle low
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return abs(distanceY) / abs(distanceX) > diffRatioThreshold
        }
    }
}