/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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