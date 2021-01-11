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
abstract class GenericAdapter<T, BindingT: ViewDataBinding>
    : RecyclerView.Adapter<GenericAdapter<T, BindingT>.GenericViewHolder>(), BaseAdapter<T> {

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

    var onClickRootLayout: (item: T) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<BindingT>(inflater, viewType, parent, false)

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

    open fun onBindFinished(item: T, holder: GenericViewHolder) {}

    inner class GenericViewHolder(
        val binding: BindingT
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T) {
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