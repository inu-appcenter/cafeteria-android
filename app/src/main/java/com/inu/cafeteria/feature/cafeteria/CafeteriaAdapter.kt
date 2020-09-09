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

import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.extension.addUrl
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.common.extension.loadFromUrl
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.repository.PrivateRepository
import kotlinx.android.synthetic.main.cafeteria_list_item.view.*
import org.koin.core.inject

class CafeteriaAdapter : BaseAdapter<Cafeteria>() {

    private val privateRepo: PrivateRepository by inject()

    internal var clickListener: (View, Cafeteria) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = parent.inflate(R.layout.cafeteria_list_item)

        return BaseViewHolder(view).apply {
            containerView.setOnClickListener { view ->
                getItem(adapterPosition)?.let { cafeteria ->
                    clickListener(view, cafeteria)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        // Use background image.
        with(view.cafeteria_image) {
            loadFromUrl(privateRepo.getServerBaseUrl().addUrl(item.backgroundImagePath))
        }

        with(view.cafeteria_title) {
            text = item.name
        }
    }
}