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
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.NavigationActivity
import com.inu.cafeteria.common.base.NavigationHostFragment
import com.inu.cafeteria.common.extension.fadeIn
import com.inu.cafeteria.common.extension.fadeOut
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.config.Config
import com.inu.cafeteria.extension.withNonNull
import com.inu.cafeteria.util.Events
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
            toolbarId = -1, // Unmanaged toolbar.
            navHostId = R.id.nav_host_cafeteria,
            tabItemId = R.id.tab_cafeteria
        ),

        /** Order notification */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_order_base,
            toolbarId = R.id.toolbar_order,
            navHostId = R.id.nav_host_order,
            tabItemId = R.id.tab_order
        ),

        /** Discount */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_discount_base,
            toolbarId = R.id.toolbar_discount,
            navHostId = R.id.nav_host_discount,
            tabItemId = R.id.tab_discount
        ),

        /** Support */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_support_base,
            toolbarId = R.id.toolbar_support,
            navHostId = R.id.nav_host_support,
            tabItemId = R.id.tab_support
        )
    )

    private val viewModel: MainViewModel by viewModels()
    private val eventHandler: LifecycleEventHandler by inject()

    /**
     * Called when brought to here by re-entering activity
     * by Firebase notification(or any other launcher intent).
     *
     * MainActivity will always be called with
     * FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP flags
     * (see [com.inu.cafeteria.common.navigation.Navigator.showMain]).
     *
     * If it is the first time the activity created, [onCreate] will be called.
     * Otherwise, [onNewIntent] will be called.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val clickAction = intent?.getStringExtra("click_action") ?: return

        handleFirebaseNotificationClickAction(clickAction)
    }

    private fun handleFirebaseNotificationClickAction(clickAction: String) {
        when (clickAction) {
            Config.handleFinishedOrderAction -> {
                Timber.i("Got action '$clickAction'. Jumping to 'order' tab!")
                jumpToTab(R.id.tab_order)
            }
            else -> {
                Timber.i("Don't know what to do with action '$clickAction'!")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventHandler.onCreate(this, savedInstanceState)

        setOfflineView()
        setWaitingOrderTabBadge()
        setSupportTabBadge()
        observeLoginEvent() // to then fetch unread answers.
    }

    private fun setOfflineView() {
        val eggs = EasterEggHelper.getOfflineViewEasterEggs(this)

        withNonNull(findViewById<CardView>(R.id.offline_view)) {
            val animation = AnimationUtils
                .loadAnimation(this@MainActivity, R.anim.shake_once)
                .apply {
                    duration = 100
                }

            setOnClickListener {
                startAnimation(animation)
                eggs.haveSomeFun()
            }

            // Apply when activity created.
            if (isOnline()) {
                fadeOut(250L)
            } else {
                fadeIn(250L)
            }
        }
    }

    private fun setWaitingOrderTabBadge() {
        setTabBadge(viewModel.numberOfFinishedOrders, R.id.tab_order)
    }

    private fun setSupportTabBadge() {
        setTabBadge(viewModel.numberOfUnreadAnswers, R.id.tab_support)
    }

    private fun setTabBadge(numberOfNotifications: LiveData<Int>, @IdRes tabItemId: Int) {
        withNonNull(findViewById<BottomNavigationView>(R.id.bottom_nav)) {
            observe(numberOfNotifications) { numberOfNotifications ->
                numberOfNotifications ?: return@observe

                if (numberOfNotifications > 0) {
                    getOrCreateBadge(tabItemId).apply {
                        backgroundColor = getColor(R.color.orange)
                        isVisible = true
                        number = numberOfNotifications
                    }
                } else {
                    getBadge(tabItemId)?.isVisible = false
                    removeBadge(tabItemId)
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

        // Apply when updated (while activity staying alive).
        withNonNull(findViewById<CardView>(R.id.offline_view)) {
            if (available) {
                fadeOut(250L)
            } else {
                fadeIn(250L)
            }
        }
    }

    override fun onInflatedBottomMenu(menu: Menu) {
        // Apply remote setting: whether to reveal or not the order feature.
        menu.findItem(R.id.tab_order)?.isVisible = Config.showOrderTab
    }

    override fun onTabSelected(item: MenuItem) {
        Events.onSelectTab(item.title.toString())
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}