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

package com.inu.cafeteria.feature.main

import android.app.Activity
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.util.ActivityHelper
import com.inu.cafeteria.util.Events
import com.inu.cafeteria.util.Fun
import com.plattysoft.leonids.ParticleSystem
import org.koin.core.KoinComponent
import org.koin.core.inject

object EasterEggHelper : KoinComponent {

    fun getOfflineViewEasterEggs(activity: Activity) = Fun(
        listOf(
            Fun.Event(9) {
                ParticleSystem(activity, 50, R.drawable.dot, 3000)
                    .setSpeedRange(0.2f, 0.7f)
                    .oneShot(activity.findViewById<CardView>(R.id.offline_view), 50)

                Events.onUserDiscoveredEasterEgg("폭죽 발견")
            },
            Fun.Event(17) {
                Toast.makeText(activity, activity.getString(R.string.egg_help), Toast.LENGTH_SHORT).show()

                Events.onUserDiscoveredEasterEgg("오프라인 뷰 연타 1")
            },
            Fun.Event(22) {
                Toast.makeText(activity, activity.getString(R.string.egg_upup), Toast.LENGTH_SHORT).show()

                Events.onUserDiscoveredEasterEgg("오프라인 뷰 연타 2")
            },
            Fun.Event(99) {
                Toast.makeText(activity, activity.getString(R.string.egg_gmg), Toast.LENGTH_SHORT).show()
                Toast.makeText(activity, activity.getString(R.string.heart), Toast.LENGTH_SHORT).show()

                Events.onUserDiscoveredEasterEgg("오프라인 뷰 연타 3")
            }
        )
    )

    fun getBarcodeCardEasterEggs() = Fun(
        listOf(
            Fun.Event(5) {
                val activity = ActivityHelper.getActivity() ?: return@Event

                Toast.makeText(activity, activity.getString(R.string.egg_lightning), Toast.LENGTH_SHORT).show()

                Events.onUserDiscoveredEasterEgg("바코드 연타")
            },

            Fun.Event(10) {
                val activity = ActivityHelper.getActivity() ?: return@Event
                val navigator: Navigator by inject()

                navigator.showPotadosDialog(activity)

                Events.onUserDiscoveredEasterEgg("감자 팝업 발견")
            }
        )
    )
}