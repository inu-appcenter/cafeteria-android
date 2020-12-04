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
import androidx.databinding.BindingAdapter
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseBindingAdapter
import com.inu.cafeteria.common.base.BaseBindingViewHolder
import com.inu.cafeteria.common.widget.AvailableTimeView
import com.inu.cafeteria.databinding.MenuBinding
import timber.log.Timber

class MenuAdapter : BaseBindingAdapter<MenuView, MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        Timber.d("Inflate Menu view holder!")

        return MenuViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = getItem(position) ?: return

        holder.bind(menu)
    }

    class MenuViewHolder(parent: ViewGroup) : BaseBindingViewHolder<MenuBinding>(parent, R.layout.menu) {

        fun bind(item: MenuView) {
            binding.menu = item

            with(binding.foods) {
                // On click
                binding.root.setOnClickListener { maxLines = 5 }
            }
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("availableAt")
        fun setAvailableAt(view: AvailableTimeView, availableAt: Int?) {
            availableAt?.let {
                view.setAvailableTime(it)
            }
        }

    }
}