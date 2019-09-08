/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.barcode

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.SingleFragmentActivity

class BarcodeActivity : SingleFragmentActivity() {
    override val fragment: Fragment = BarcodeFragment()
    override val layoutId: Int? = R.layout.transparent_fragment_activity

    companion object {
        fun callingIntent(context: Context) = Intent(context, BarcodeActivity::class.java)
    }
}