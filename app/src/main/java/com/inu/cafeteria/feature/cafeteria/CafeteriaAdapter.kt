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

package com.inu.cafeteria.feature.cafeteria

import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.PositionRetainingAdapter
import com.inu.cafeteria.common.base.PositionRetainingViewHolder
import com.inu.cafeteria.common.extension.onScrollStateChange
import com.inu.cafeteria.extension.withNonNull
import timber.log.Timber

class CafeteriaAdapter : PositionRetainingAdapter<CafeteriaView>() {

    private val menuPagePool = RecyclerView.RecycledViewPool()
    private val menuPool = RecyclerView.RecycledViewPool()

    var onClickMore: (CafeteriaView) -> Any? = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionRetainingViewHolder {
        return CafeteriaViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PositionRetainingViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        with(holder as CafeteriaViewHolder) {
            bind(getItem(position))
        }
    }

    inner class CafeteriaViewHolder(parent: ViewGroup) : PositionRetainingViewHolder(parent, R.layout.cafeteria) {

        override val layoutManager: LinearLayoutManager? =
            view.findViewById<RecyclerView>(R.id.menu_page_recycler)?.layoutManager as? LinearLayoutManager

        private val menuPageAdapter = MenuPageAdapter(menuPool)

        init {
            Timber.d("Inflate Cafeteria view holder!")

            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            withNonNull(view.findViewById(R.id.menu_page_recycler) as RecyclerView) {
                onScrollStateChange { saveViewHolderPosition(this@CafeteriaViewHolder) }

                adapter = menuPageAdapter.apply {
                    emptyView = itemView.findViewById(R.id.empty_cafeteria_view)
                }

                setRecycledViewPool(menuPagePool)

                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        fun bind(cafeteria: CafeteriaView?) {
            cafeteria ?: return

            withNonNull(view.findViewById<TextView>(R.id.cafeteria_name)) {
                text = cafeteria.name
            }

            withNonNull(view.findViewById<ImageButton>(R.id.more_button)) {
                setOnClickListener {
                    onClickMore(cafeteria)
                }
            }

            menuPageAdapter.data = cafeteria.wholeMenus
        }
    }
}