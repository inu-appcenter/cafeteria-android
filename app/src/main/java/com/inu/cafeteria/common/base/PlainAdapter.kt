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

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.extension.setVisible
import org.koin.core.KoinComponent
import timber.log.Timber

abstract class PlainAdapter<T, VH: PlainViewHolder<*>> : RecyclerView.Adapter<VH>(), KoinComponent {

    /**
     * loadingView will be shown when it is not null and isLoading is true.
     */
    var loadingView: View? = null
        set(value) {
            field = value
            updatePeripheralViews()
        }

    var isLoading: Boolean = false
        set(value) {
            field = value
            updatePeripheralViews()
        }

    /**
     * emptyView will be shown when it is not null and data.isEmpty() returns true
     */
    var emptyView: View? = null
        set(value) {
            field = value
            updatePeripheralViews()
        }

    var data: List<T> = ArrayList()
        set(value) {
            if (field === value) return
            field = value

            // Don'nt know why but...
            // this preserves RecyclerView position when changing date in CafeteriaFragment.
            notifyItemRangeChanged(0, value.size)
            updatePeripheralViews()
        }

    @CallSuper
    protected open fun updatePeripheralViews() {
        emptyView?.setVisible(data.isEmpty() && !isLoading)
        loadingView?.setVisible(isLoading)
    }

    fun getItem(position: Int): T? {
        if (position < 0) {
            Timber.w("Trying to access index $position.")
            return null
        }

        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}