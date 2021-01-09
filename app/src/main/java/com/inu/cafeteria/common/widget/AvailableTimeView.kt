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
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.inu.cafeteria.R
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.common.extension.removeFromParent
import com.inu.cafeteria.extension.forEachBits

class AvailableTimeView(
    context: Context,
    attributeSet: AttributeSet
) : LinearLayout(context, attributeSet) {

    private val viewCache: MutableMap<Int, View> = mutableMapOf()

    // Prevent unnecessary works.
    private var lastAvailableTime = 0
    private var lastLayoutKey = 0

    fun setAvailableTime(availableTimes: Int) {
        if (availableTimes == lastAvailableTime) {
            return
        }

        prepareProperLayout(availableTimes.countOneBits())

        val slotIds = listOf(
            R.id.squircle_slot_0,
            R.id.squircle_slot_1,
            R.id.squircle_slot_2
        ).iterator()

        availableTimes.forEachBits((0..2)) { time ->
            val id = slotIds.next()
            val view = findViewById<ImageView>(id)

            view?.setImageResource(getImageForTime(time))
        }

        lastAvailableTime = availableTimes
    }

    private fun getImageForTime(time: Int): Int {
        return when (time) {
            BREAKFAST -> R.drawable.morning
            LUNCH -> R.drawable.day
            DINNER -> R.drawable.night
            else -> R.drawable.no_img
        }
    }

    private fun prepareProperLayout(howMany: Int) {
        if (howMany == lastLayoutKey) {
            return
        }

        removeAllViews()

        addView(bringView(howMany).apply { removeFromParent() })

        lastLayoutKey = howMany
    }

    private fun bringView(howMany: Int): View {
        val viewInCache = viewCache[howMany]
        if (viewInCache != null) {
            return viewInCache
        }

        return inflateView(howMany).apply {
            viewCache[howMany] = this
        }
    }

    private fun inflateView(howMany: Int): View {
        val layoutId = when (howMany) {
            1 -> R.layout.squircle_single
            2 -> R.layout.squircle_double
            3 -> R.layout.squircle_triple
            else -> R.layout.squircle_single
        }

        return inflate(layoutId)
    }

    companion object {
        private const val BREAKFAST = 0x04
        private const val LUNCH = 0x02
        private const val DINNER = 0x01
    }
}