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
import com.inu.cafeteria.common.base.AsyncBindingViewHolder
import com.inu.cafeteria.common.base.BaseBindingAdapter
import com.inu.cafeteria.common.widget.AsyncFrameLayout
import com.inu.cafeteria.common.widget.AvailableTimeView
import com.inu.cafeteria.databinding.MenuBinding
import com.inu.cafeteria.extension.withNonNull
import timber.log.Timber

class MenuAdapter : BaseBindingAdapter<MenuView, MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        Timber.d("Inflate Menu view holder!")

        val itemView = AsyncFrameLayout(parent.context)
        itemView.inflateAsync(R.layout.menu)

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = getItem(position) ?: return

        holder.asyncView.invokeWhenInflated {
            holder.createBinding(R.id.root_layout)
            post {
                holder.bind(menu)
            }
        }
    }

    class MenuViewHolder(val asyncView: AsyncFrameLayout) : AsyncBindingViewHolder<MenuBinding>(asyncView) {

        fun bind(item: MenuView) {
            Timber.e("YEAH BIND!!! binding: ${binding != null}")

            withNonNull(binding) {
                menu = item

                with(foods) {
                    maxLines = 2

                    // On click
                    root.setOnClickListener { maxLines = 5 }
                }
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