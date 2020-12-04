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

package com.inu.cafeteria.common.base

import android.util.SparseIntArray
import androidx.annotation.CallSuper
import timber.log.Timber

/**
 * This adapter handles loading/empty view visibility.
 */
abstract class PositionRetainingAdapter<T> : PlainAdapter<T, PositionRetainingViewHolder>() {

    var positions = SparseIntArray()

    /**
     * This must be called when an inner RecyclerView is scrolled.
     */
    fun saveViewHolderPosition(holder: PositionRetainingViewHolder) {
        val position = holder.adapterPosition
        val firstVisiblePosition = holder.layoutManager?.findFirstVisibleItemPosition() ?: -1

        Timber.i("Save cafeteria menu page ${position}'s page number: $firstVisiblePosition")

        positions.put(position, firstVisiblePosition)
    }

    private fun restoreViewHolderPosition(holder: PositionRetainingViewHolder) {
        positions.get(holder.adapterPosition, -1).takeIf { it >= 0 }?.let {
            Timber.i("Restore holder's page number: $it")

            holder.layoutManager?.scrollToPositionWithOffset(it, 0)
        }
    }

    @CallSuper
    override fun onBindViewHolder(holder: PositionRetainingViewHolder, position: Int) {
        restoreViewHolderPosition(holder)
    }

    override fun onViewRecycled(holder: PositionRetainingViewHolder) {
        saveViewHolderPosition(holder)

        super.onViewRecycled(holder)
    }
}