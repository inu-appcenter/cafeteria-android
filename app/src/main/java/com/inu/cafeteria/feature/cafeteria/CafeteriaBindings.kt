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

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.model.json.Cafeteria
import timber.log.Timber

@BindingAdapter("cafeteria")
fun setCafeteria(listView: RecyclerView, data: List<Cafeteria>?) {
    val adapter = listView.adapter as? CafeteriaAdapter
    if (adapter == null) {
        Timber.w("Adapter not set.")
        return
    }

    adapter.data = data.orEmpty()

    Timber.i("Cafeteria is updated.")
}

@BindingAdapter("corners")
fun setCorners(listView: RecyclerView, data: FoodMenu?) {
    val adapter = listView.adapter as? CornersAdapter
    if (adapter == null) {
        Timber.w("Adapter not set.")
        return
    }

    adapter.data = data?.corners.orEmpty()

    Timber.i("Corners is(are) updated.")
}
