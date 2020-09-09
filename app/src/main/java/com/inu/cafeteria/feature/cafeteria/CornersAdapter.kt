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
import com.inu.cafeteria.common.base.FooterAdapter
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.model.FoodMenu
import kotlinx.android.synthetic.main.corner_list_item.view.*

class CornersAdapter : FooterAdapter<FoodMenu.Corner>() {

    override val footerLayoutId: Int = R.layout.cafeteria_list_footer

    override fun onCreateContentViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = parent.inflate(R.layout.corner_list_item)

        return BaseViewHolder(view)
    }

    override fun onBindContentViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        with(view.corner_title) {
            text = item.title
        }

        with(view.menu) {
            item.menu.joinToString(" ").also {
                if (it.isBlank()) {
                    text = context.getString(R.string.desc_no_data)
                    alpha = 0.6f
                } else {
                    text = it
                    alpha = 1.0f
                }
            }
        }
    }

    override fun onBindFooterViewHolder(holder: BaseViewHolder) {
        // Nothing to do with footer view.
        // The text is declared in xml.
    }
}