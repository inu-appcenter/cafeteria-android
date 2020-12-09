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

package com.inu.cafeteria.feature.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.common.notification.ClickActionHelper
import org.koin.android.ext.android.inject
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkIntent(intent)) {
            return
        }

        navigator.showMain()
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent?): Boolean {
        intent ?: return false

        if (!intent.hasExtra("click_action")) {
            return false
        }

        val action = intent.getStringExtra("click_action")

        Timber.i("Starting from click_action(${action})!")

        ClickActionHelper.startActivity(
            action,
            intent.extras,
            this
        )

        finish()

        return true
    }
}