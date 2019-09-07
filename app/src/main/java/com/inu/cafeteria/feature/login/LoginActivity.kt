/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.login

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inu.cafeteria.common.base.SingleFragmentActivity

class LoginActivity : SingleFragmentActivity() {
    override val fragment: Fragment = LoginFragment()

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}