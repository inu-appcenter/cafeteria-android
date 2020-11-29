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

package com.inu.cafeteria.feature.support.notice

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.extension.isVisible
import kotlinx.android.synthetic.main.notice_item.view.*


class NoticeAdapter : BaseAdapter<NoticeView, NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        return NoticeViewHolder(parent, R.layout.notice_item)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.bind(item)
    }

    inner class NoticeViewHolder(parent: ViewGroup, @LayoutRes layoutId: Int) : BaseViewHolder(parent, layoutId) {

        fun bind(item: NoticeView) {
            with(itemView.date) {
                text = item.date
            }

            with(itemView.title) {
                text = item.title
            }

            with(itemView.body) {
                isVisible = item.expanded
                text = item.body
            }

            with(itemView.more_close) {
                setImageResource(
                    when (item.expanded) {
                        true -> R.drawable.ic_keyboard_arrow_up_24px
                        else -> R.drawable.ic_keyboard_arrow_down_24px
                    }
                )
            }

            with(itemView.summary) {
                setOnClickListener {
                    item.expanded = !item.expanded

                    itemView.body.isVisible = item.expanded
                }
            }
        }
    }
}