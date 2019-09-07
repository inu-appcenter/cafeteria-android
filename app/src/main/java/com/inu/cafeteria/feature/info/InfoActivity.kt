/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.SingleFragmentActivity
import com.inu.cafeteria.common.extension.setSupportActionBar
import kotlinx.android.synthetic.main.toolbar.*

class InfoActivity : SingleFragmentActivity() {
    override val fragment: Fragment = InfoFragment()
    override val layoutId: Int? = R.layout.toolbar_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar, title = false, upButton = true)
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, InfoActivity::class.java)
    }
}