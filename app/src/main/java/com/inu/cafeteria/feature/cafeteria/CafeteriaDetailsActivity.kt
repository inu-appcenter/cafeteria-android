/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.cafeteria

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inu.cafeteria.common.base.SingleFragmentActivity
import com.inu.cafeteria.model.json.Cafeteria

class CafeteriaDetailsActivity : SingleFragmentActivity() {
    // Go lazy or it will be null.
    override val fragment: Fragment by lazy {
        CafeteriaDetailFragment.forCafeteria(
            intent.getSerializableExtra(INTENT_EXTRA_PARAM_CAFETERIA) as Cafeteria
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instantiated = true
    }

    override fun onDestroy() {
        super.onDestroy()
        instantiated = false
    }

    companion object {
        fun callingIntent(context: Context, cafeteria: Cafeteria): Intent {
            return Intent(context, CafeteriaDetailsActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_CAFETERIA, cafeteria)
            }
        }

        private const val INTENT_EXTRA_PARAM_CAFETERIA = "org.inu.INTENT_PARAM_CAFETERIA"

        var instantiated: Boolean = false
            private set
    }
}

