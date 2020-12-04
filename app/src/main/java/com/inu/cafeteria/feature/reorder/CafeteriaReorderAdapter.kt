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

package com.inu.cafeteria.feature.reorder

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseBindingAdapter
import com.inu.cafeteria.common.base.BaseBindingViewHolder
import com.inu.cafeteria.common.widget.ItemTouchHelperAdapter
import com.inu.cafeteria.common.widget.ItemTouchHelperViewHolder
import com.inu.cafeteria.databinding.CafeteriaReorderItemBinding
import java.util.*

class CafeteriaReorderAdapter(
    private val onDragStart: (RecyclerView.ViewHolder) -> Any?
) : BaseBindingAdapter<CafeteriaReorderView, CafeteriaReorderAdapter.ReorderViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReorderViewHolder(parent)

    override fun onBindViewHolder(holder: ReorderViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.bind(item)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        data = data.minusElement(data[position])
        updatePeripheralViews()
        notifyItemRemoved(position)
    }

    inner class ReorderViewHolder(parent: ViewGroup) : BaseBindingViewHolder<CafeteriaReorderItemBinding>(parent, R.layout.cafeteria_reorder_item), ItemTouchHelperViewHolder {

        fun bind(item: CafeteriaReorderView) {
            binding.cafeteria = item

            with(binding.handle) {
                setOnTouchListener { v, event ->
                    v.performClick()
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        onDragStart(this@ReorderViewHolder)
                    }
                    false
                }
            }
        }

        override fun onItemSelected() {
            with(binding.root) {
                setBackgroundResource(R.color.selectedItemBackground)
            }
        }

        override fun onItemClear() {
            with(binding.root) {
                setBackgroundColor(0)
            }
        }
    }
}