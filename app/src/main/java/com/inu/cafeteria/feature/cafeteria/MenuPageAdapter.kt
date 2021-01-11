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
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseBindingAdapter
import com.inu.cafeteria.common.base.BaseBindingViewHolder
import com.inu.cafeteria.common.extension.setLeftInsetDivider
import com.inu.cafeteria.databinding.MenuPageBinding
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.min

class MenuPageAdapter(
    private val menuPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
    private val pageSize: Int = DEFAULT_PAGE_SIZE
) : BaseBindingAdapter<MenuView, MenuPageAdapter.MenuPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MenuPageViewHolder(parent)

    override fun onBindViewHolder(holder: MenuPageViewHolder, position: Int) {
        holder.bind(
            if (pageSize == 0) items else paginateProps(position)
        )
    }

    private fun paginateProps(pageNumber: Int): List<MenuView> {
        val indexStart = pageNumber * pageSize
        val indexEnd = min(indexStart + pageSize - 1, items.size - 1)

        return items.slice(indexStart..indexEnd)
    }

    override fun getItemCount(): Int {
        return if (pageSize == 0) 1 else ceil(items.size.toDouble() / pageSize).toInt()
    }

    inner class MenuPageViewHolder(parent: ViewGroup) : BaseBindingViewHolder<MenuPageBinding>(parent, R.layout.menu_page) {

        private val menuAdapter = MenuAdapter()

        init {
            Timber.d("Inflate Menu Page view holder!")
            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(binding.menuRecycler) {
                adapter = menuAdapter

                setRecycledViewPool(menuPool)

                setLeftInsetDivider(R.drawable.line_divider, R.dimen.menu_left_margin_until_text, pageSize - 1)
            }
        }

        fun bind(pagedMenus: List<MenuView>) {
            menuAdapter.items = pagedMenus
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE: Int = 2
    }
}