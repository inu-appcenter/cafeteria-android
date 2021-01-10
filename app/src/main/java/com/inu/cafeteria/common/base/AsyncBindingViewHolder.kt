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
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * This is for adapters who inflate views asynchronously.
 * The 'view' passed to the constructor works as a wrapper of a real view.
 * Once it is sure that the real view is inflated and added to the view,
 * user can call 'createBinding' to create a binding from that view.
 */
open class AsyncBindingViewHolder<T: ViewDataBinding>(private val view: View) : RecyclerView.ViewHolder(view) {

    protected var binding: T? = null
        private set

    /**
     * Create a binding from its item view.
     *
     * @param rootId: id of root view of the real view(must be a binding layout).
     */
    fun createBinding(@IdRes rootId: Int) {
        binding = DataBindingUtil.bind<T>(view.findViewById(rootId))
    }
}
