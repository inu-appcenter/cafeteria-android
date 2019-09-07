/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.info

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.model.OpenSourceSoftware
import kotlinx.android.synthetic.main.license_list_item.view.*
import org.koin.core.inject

class LicensesAdapter : BaseAdapter<OpenSourceSoftware>() {

    private val navigator: Navigator by inject()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent.inflate(R.layout.license_list_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        with(view.name) {
            text = item.name
        }

        with(view.source_url) {
            text = item.sourceCodeReference
            setOnClickListener { navigator.showUrl(item.sourceCodeReference) }
        }

        with(view.copyright) {
            text = item.copyright
        }

        with(view.license) {
            text = item.licenseName
            setOnClickListener { navigator.showUrl(item.licenseReference) }
        }
    }
}