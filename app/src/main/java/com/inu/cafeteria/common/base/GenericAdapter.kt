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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.extension.setVisible

/**
 * Complete generic adapter.
 */
abstract class GenericAdapter<E, T: ViewDataBinding> : RecyclerView.Adapter<GenericAdapter<E, T>.GenericViewHolder>() {

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

    var items: List<E> = listOf()
        set(value) {
            field = value

            notifyDataSetChanged()
            updatePeripheralViews()
        }

    @CallSuper
    protected open fun updatePeripheralViews() {
        emptyView?.setVisible(items.isEmpty() && !isLoading)
        loadingView?.setVisible(isLoading)
    }

    var onClickRootLayout: (item: E) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<T>(inflater, viewType, parent, false)

        return GenericViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    abstract fun getLayoutIdForPosition(position: Int): Int

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)

        onBindFinished(item, holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open fun onBindFinished(item: E, holder: GenericViewHolder) {}

    inner class GenericViewHolder(
        val binding: T
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: E) {
            with(this.binding) {
                setVariable(BR.item, item)
                executePendingBindings()
            }

            with(this.binding.root) {
                setOnClickListener { onClickRootLayout(item) }
            }
        }
    }
}