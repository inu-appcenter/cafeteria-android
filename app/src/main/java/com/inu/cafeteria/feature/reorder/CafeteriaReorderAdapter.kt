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

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.widget.ItemTouchHelperAdapter
import kotlinx.android.synthetic.main.cafeteria.view.cafeteria_name
import kotlinx.android.synthetic.main.cafeteria_reorder_item.view.*
import java.util.*

class CafeteriaReorderAdapter(
    private val onStartDrag: (viewHolder: RecyclerView.ViewHolder) -> Any?
) : RecyclerView.Adapter<CafeteriaReorderAdapter.CafeteriaSortViewHolder>(), ItemTouchHelperAdapter {

    var cafeteria: MutableList<CafeteriaReorderView> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaSortViewHolder {
        return CafeteriaSortViewHolder(parent).also {
            it.itemView.handle.setOnTouchListener { v, event ->
                v.performClick()
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDrag(it)
                }
                false
            }
        }
    }

    override fun onBindViewHolder(holder: CafeteriaSortViewHolder, position: Int) {
        holder.bind(cafeteria[position])
    }

    override fun getItemCount(): Int {
        return cafeteria.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(cafeteria, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(cafeteria, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        cafeteria.removeAt(position);
        notifyItemRemoved(position);
    }

    inner class CafeteriaSortViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.cafeteria_reorder_item, parent, false))

        fun bind(item: CafeteriaReorderView) {
            with(itemView.cafeteria_name) {
                text = item.displayName
            }
        }
    }
}