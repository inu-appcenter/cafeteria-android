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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.feature.main.CafeteriaAdapter
import org.koin.core.KoinComponent
import timber.log.Timber

/**
 * This adapter handles loading/empty view visibility.
 */
abstract class PositionRetainingAdapter<T> : BaseAdapter<T, PositionRetainingViewHolder>() {

    var positions = SparseIntArray()

    fun saveViewHolderPosition(holder: PositionRetainingViewHolder) {
        val position = holder.adapterPosition
        val firstVisiblePosition = holder.layoutManager?.findFirstVisibleItemPosition() ?: -1

        positions.put(position, firstVisiblePosition)
    }

    fun restoreViewHolderPosition(holder: PositionRetainingViewHolder) {
        positions.get(holder.adapterPosition, 0).takeIf { it >= 0 }?.let {
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