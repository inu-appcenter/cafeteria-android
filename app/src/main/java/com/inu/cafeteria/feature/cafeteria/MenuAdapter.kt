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
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.base.DefaultAdapter
import com.inu.cafeteria.common.extension.setVisible
import kotlinx.android.synthetic.main.menu.view.*
import timber.log.Timber

class MenuAdapter : DefaultAdapter<MenuView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        Timber.d("Inflate Menu view holder!")

        return BaseViewHolder(parent, R.layout.menu)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val menu = getItem(position) ?: return

        with(holder.view) {

            with(available_at) {
                setAvailableTime(menu.availableAt)
            }

            with(corner_name) {
                text = menu.cornerName
            }

            with(foods) {
                text = menu.foods

                // Initial
                maxLines = 2

                // On click
                holder.view.setOnClickListener { maxLines = 5 }
            }

            with(price) {
                setVisible(menu.price ?: 0 != 0)
                text = context.getString(R.string.unit_krw, menu.price)
            }

            with(calorie) {
                setVisible(menu.calorie ?: 0 != 0)
                text = context.getString(R.string.unit_cal, menu.calorie)
            }

            with(separator) {
                setVisible((menu.price ?: 0 != 0) && (menu.calorie ?: 0 != 0))
            }
        }
    }
}