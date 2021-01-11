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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.inu.cafeteria.R
import com.inu.cafeteria.common.extension.removeFromParent
import com.inu.cafeteria.extension.forEachBits

/**
 * [setAvailableTime] works asynchronously.
 */
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

        prepareProperView(availableTimes.countOneBits()) {
            setAvailableTimeSlots(availableTimes)
        }

        lastAvailableTime = availableTimes
    }

    private fun prepareProperView(howMany: Int, onProperViewReady: () -> Unit) {
        if (howMany == lastLayoutKey) {
            return
        }

        removeAllViews()

        bringViewCached(howMany) {
            addView(it)

            onProperViewReady()
        }

        lastLayoutKey = howMany
    }

    private fun bringViewCached(howMany: Int, onViewReady: (View) -> Unit) {
        val fromCache = viewCache[howMany]

        if (fromCache != null) {
            fromCache.removeFromParent() // Might not have been removed early.
            onViewReady(fromCache)
            return
        }

        inflateView(howMany) {
            viewCache[howMany] = it

            onViewReady(it)
        }
    }

    private fun inflateView(howMany: Int, onInflateFinished: (View) -> Unit) {
        val layoutId = when (howMany) {
            1 -> R.layout.squircle_single
            2 -> R.layout.squircle_double
            3 -> R.layout.squircle_triple
            else -> R.layout.squircle_single
        }

        val inflater = AsyncLayoutInflater(context)

        inflater.inflate(layoutId, this) { view: View, _: Int, _: ViewGroup? ->
            onInflateFinished(view)
        }
    }

    private fun setAvailableTimeSlots(availableTimes: Int) {
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
    }

    private fun getImageForTime(time: Int): Int {
        return when (time) {
            BREAKFAST -> R.drawable.morning
            LUNCH -> R.drawable.day
            DINNER -> R.drawable.night
            else -> R.drawable.no_img
        }
    }

    companion object {
        private const val BREAKFAST = 0x04
        private const val LUNCH = 0x02
        private const val DINNER = 0x01
    }
}