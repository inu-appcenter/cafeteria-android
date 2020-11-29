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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.NavigationActivity
import com.inu.cafeteria.common.base.NavigationHostFragment
import com.inu.cafeteria.common.extension.fadeIn
import com.inu.cafeteria.common.extension.fadeOut
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.navigation.rootDestinations
import com.inu.cafeteria.util.Fun
import com.plattysoft.leonids.ParticleSystem
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class MainActivity : NavigationActivity() {

    override val menuRes: Int = R.menu.bottom_menu
    override val layoutRes: Int = R.layout.main_activity
    override val mainPagerRes: Int = R.id.main_pager
    override val bottomNavRes: Int = R.id.bottom_nav

    override val fragmentArguments: List<NavigationHostFragment.Arguments> = listOf(

        /** Home */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_home_base,
            toolbarId = -1, // Un-managed toolbar.
            navHostId = R.id.nav_host_cafeteria,
            tabItemId = R.id.tab_cafeteria,
            rootDests = rootDestinations
        ),

        /** Discount */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_discount_base,
            toolbarId = R.id.toolbar_discount,
            navHostId = R.id.nav_host_discount,
            tabItemId = R.id.tab_discount,
            rootDests = rootDestinations
        ),

        /** Support */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_support_base,
            toolbarId = R.id.toolbar_support,
            navHostId = R.id.nav_host_support,
            tabItemId = R.id.tab_support,
            rootDests = rootDestinations
        )
    )

    private val viewModel: MainViewModel by viewModels()
    private val eventHandler: LifecycleEventHandler by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventHandler.onCreate(this)

        setOfflineView()
        setSupportTabBadge()
        observeLoginEvent() // to fetch notifications(unread answers).
    }

    private fun setOfflineView() {
        val eggs = getEasterEggs()

        with(offline_view) {
            val animation = AnimationUtils
                .loadAnimation(this@MainActivity, R.anim.shake_once)
                .apply {
                    duration = 100
                }

            setOnClickListener {
                startAnimation(animation)
                eggs.haveSomeFun()
            }
        }
    }

    private fun getEasterEggs() = Fun(
        listOf(
            Fun.Event(9) {
                Toast.makeText(this, getString(R.string.egg_wow), Toast.LENGTH_SHORT).show()

                ParticleSystem(this, 50, R.drawable.dot, 3000)
                    .setSpeedRange(0.2f, 0.7f)
                    .oneShot(offline_view, 50)
            },
            Fun.Event(17) {
                Toast.makeText(this, getString(R.string.egg_help), Toast.LENGTH_SHORT).show()
            },
            Fun.Event(22) {
                Toast.makeText(this, getString(R.string.egg_upup), Toast.LENGTH_SHORT).show()
            },
            Fun.Event(99) {
                Toast.makeText(this, getString(R.string.egg_gmg), Toast.LENGTH_SHORT).show()
                Toast.makeText(this, getString(R.string.heart), Toast.LENGTH_SHORT).show()
            }
        )
    )

    private fun setSupportTabBadge() {
        with(bottom_nav) {

            observe(viewModel.numberOfUnreadAnswers) { numberOfNotifications ->
                numberOfNotifications ?: return@observe

                val badge = getOrCreateBadge(R.id.tab_support).apply {
                    backgroundColor = getColor(R.color.orange)
                }

                Timber.i("Notifications left: $numberOfNotifications")

                with(badge) {
                    isVisible = numberOfNotifications > 0
                    number = numberOfNotifications
                }
            }
        }
    }

    private fun observeLoginEvent() {
        with(viewModel) {
            observe(loggedInStatus) {
                it?.takeIf { it }?.let {
                    onLoggedIn()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        eventHandler.onResume(this)
    }

    override fun onPause() {
        super.onPause()

        eventHandler.onPause(this)
    }

    override fun onNetworkStateChange(available: Boolean) {
        if (available) {
            viewModel.load(this)
        }

        with(offline_view) {
            if (available) {
                fadeOut(250L)
            } else {
                fadeIn(250L)
            }
        }
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}