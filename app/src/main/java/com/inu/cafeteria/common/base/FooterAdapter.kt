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
import android.view.ViewGroup
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.common.extension.isVisible

/**
 * Base adapter with footer!
 */

abstract class FooterAdapter<T> : BaseAdapter<T>() {

    abstract val footerLayoutId: Int

    private var footerView: View? = null

    override fun setEmptyView(isDataEmpty: Boolean) {
        super.setEmptyView(isDataEmpty)
        footerView?.isVisible = !isDataEmpty
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) TYPE_FOOTER else TYPE_CONTENT
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == TYPE_FOOTER) {
            val view = parent.inflate(footerLayoutId).apply { isVisible = data.isNotEmpty() }
            footerView = view
            BaseViewHolder(view)
        } else {
            onCreateContentViewHolder(parent)
        }
    }

    final override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            onBindFooterViewHolder(holder)
        } else {
            onBindContentViewHolder(holder, position)
        }
    }

    abstract fun onCreateContentViewHolder(parent: ViewGroup): BaseViewHolder

    abstract fun onBindContentViewHolder(holder: BaseViewHolder, position: Int)
    abstract fun onBindFooterViewHolder(holder: BaseViewHolder)

    companion object {
        private val TYPE_CONTENT = 0
        private val TYPE_FOOTER = 1
    }
}