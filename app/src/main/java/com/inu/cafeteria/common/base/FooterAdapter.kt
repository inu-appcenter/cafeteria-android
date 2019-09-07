/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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