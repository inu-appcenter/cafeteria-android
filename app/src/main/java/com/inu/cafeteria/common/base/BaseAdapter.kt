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

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.extension.setVisible
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Base RecyclerView.Adapter that provides some convenience when creating a new Adapter, such as
 * data list handing and item animations
 */

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder>(), KoinComponent {
    private val mContext: Context by inject()

    var data: MutableList<T> = ArrayList()
        set(value) {
            if (field === value) return

            field = value

            notifyDataSetChanged()

            setEmptyView(value.isEmpty())
        }

    @CallSuper
    protected open fun setEmptyView(isDataEmpty: Boolean) {
        emptyView?.setVisible(isDataEmpty)
    }

    /**
     * This view can be set, and the adapter will automatically control the visibility of this view
     * based on the data
     */

    var emptyView: View? = null
        set(value) {
            field = value
            field?.setVisible(data.isEmpty())
        }

    fun getItem(position: Int): T? {
        if (position < 0) {
            Timber.w("Trying to access index $position!!")
            return null
        }

        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}