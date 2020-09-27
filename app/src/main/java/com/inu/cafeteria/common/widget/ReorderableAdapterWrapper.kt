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

package com.inu.cafeteria.common.widget

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * A RecyclerView.Adapter wrapper that supports item reordering by using ItemTouchHelper.
 * The adapter must implement ItemTouchHelperAdapter.
 */
class ReorderableAdapterWrapper<T>(
    adapterFactory: (ItemTouchHelper) -> T,
    private val onItemChange: (T) -> Any? = {}
) where T: RecyclerView.Adapter<*>, T: ItemTouchHelperAdapter {

    private val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback())
    val adapter = adapterFactory(itemTouchHelper)

    fun setWithRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    inner class ItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            onItemChange(adapter)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
            onItemChange(adapter)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder is ItemTouchHelperViewHolder) {
                    viewHolder.onItemSelected()
                }
            }

            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            if (viewHolder is ItemTouchHelperViewHolder) {
                viewHolder.onItemClear()
            }
        }
    }
}