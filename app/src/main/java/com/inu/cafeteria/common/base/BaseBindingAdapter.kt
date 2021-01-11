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

/**
 * A base adapter that uses BaseBindingViewHolder, which has ViewDataBinding instance.
 */
abstract class BaseBindingAdapter<T, VH: RecyclerView.ViewHolder>
    : RecyclerView.Adapter<VH>(), BaseAdapter<T>, KoinComponent {

    override var loadingView: View? = null
        set(value) {
            field = value
            updatePeripheralViews()
        }

    override var isLoading: Boolean = false
        set(value) {
            field = value
            updatePeripheralViews()
        }

    override var emptyView: View? = null
        set(value) {
            field = value
            updatePeripheralViews()
        }

    override var items: List<T> = listOf()
        set(value) {
            val old = field

            field = value

            onItemsChanged(old, value)
            updatePeripheralViews()
        }

    override fun onItemsChanged(old: List<T>, new: List<T>) {
        notifyDataSetChanged()
    }

    @CallSuper
    protected open fun updatePeripheralViews() {
        emptyView?.setVisible(items.isEmpty() && !isLoading)
        loadingView?.setVisible(isLoading)
    }

    fun getItem(position: Int): T? {
        if (position < 0) {
            Timber.w("Trying to access index $position.")
            return null
        }

        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}