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
import androidx.constraintlayout.widget.ConstraintLayout
import com.inu.cafeteria.R
import com.inu.cafeteria.extension.forEachBits
import com.inu.cafeteria.extension.has
import timber.log.Timber

class AvailableTimeView(
    context: Context,
    attributeSet: AttributeSet
) : LinearLayout(context, attributeSet) {

    init {
        initView()
        Timber.i("YEAH!!")
    }

    private fun initView() {
        inflate(context, R.layout.available_time_view, this)
        setAvailableTime(if (isInEditMode) 7 else 0)
    }

    fun setAvailableTime(availableTimes: Int) {
        val layout = selectProperLayout(availableTimes.countOneBits())

        val imageViewsIterator = listOf<ImageView?>(
            layout.findViewById(R.id.squircle_slot_0),
            layout.findViewById(R.id.squircle_slot_1),
            layout.findViewById(R.id.squircle_slot_2)
        ).iterator()

        availableTimes.forEachBits((0..2)) { time ->
            imageViewsIterator.next()?.setImageResource(getImageForTime(time))
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

    private fun selectProperLayout(howMany: Int): ConstraintLayout {
        val layouts = listOf<ConstraintLayout>(
            findViewById(R.id.single_view),
            findViewById(R.id.double_view),
            findViewById(R.id.triple_view)
        ).apply { forEach { it.visibility = GONE } }

        return if (howMany in 1..3) { layouts[howMany-1] } else { layouts[0] }
            .apply { visibility = VISIBLE }
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