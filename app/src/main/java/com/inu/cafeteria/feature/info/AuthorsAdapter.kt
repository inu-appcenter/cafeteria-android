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

package com.inu.cafeteria.feature.info

import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.common.extension.partialBold
import com.inu.cafeteria.injection.ME
import com.inu.cafeteria.model.AuthorGroup
import kotlinx.android.synthetic.main.author_list_item.view.*

class AuthorsAdapter : BaseAdapter<AuthorGroup>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent.inflate(R.layout.author_list_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        with(view.phase) {
            text = context.getString(R.string.phase, item.phase)
        }

        with(view.authors) {
            val original = item.authors.joinToString(separator = "\n") { it.name + "-" + it.part }
            text = original
            setOnClickListener { text = original.partialBold(ME.name) }
        }
    }
}