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

package com.inu.cafeteria.common.base

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.widget.BackStack
import com.inu.cafeteria.common.widget.NonSwipingViewPager
import java.util.*

/**
 * A base Activity that acts as a host of bottom navigation.
 */
abstract class NavigationActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    private val backStack = BackStack<Int>()

    abstract val fragmentArguments: List<NavigationHostFragment.Arguments>
    abstract val menuRes: Int
    abstract val layoutRes: Int
    abstract val mainPagerRes: Int
    abstract val bottomNavRes: Int

    private lateinit var mainPager: NonSwipingViewPager
    private lateinit var bottomNavigation: BottomNavigationView

    private var lastRootLevelBackPress = 0L

    /**
     * For children.
     * Use this method to completely mock user's tab touch behavior.
     */
    protected fun jumpToTab(@IdRes tabItemId: Int) {
        val menuItem = bottomNavigation.menu.findItem(tabItemId)

        selectTabByMenuItem(menuItem)
    }

    /**
     * For children.
     * Override this to get notified when a user selects a bottom navigation(tab).
     */
    protected open fun onTabSelected(item: MenuItem) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        initViewPager(savedInstanceState)
        initBottomNavigation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        backStack.saveBackStack(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        backStack.restoreBackStack(savedInstanceState)
    }

    private fun initViewPager(savedInstanceState: Bundle?) {
        mainPager = findViewById(mainPagerRes)

        with(mainPager) {
            adapter = ViewPagerAdapter()
            offscreenPageLimit = fragmentArguments.size

            if (savedInstanceState == null) {
                checkDeepLink()
            }
        }
    }

    private fun checkDeepLink() {
        getAllFragments().forEachIndexed { position, fragment ->
            val hasDeepLink = fragment.handleDeepLink(intent)

            if (hasDeepLink) {
                selectTab(position)
            }
        }
    }

    private fun initBottomNavigation() {
        bottomNavigation = findViewById(bottomNavRes)

        with(bottomNavigation) {
            menu.clear()
            inflateMenu(menuRes)

            setOnNavigationItemSelectedListener(this@NavigationActivity)
            setOnNavigationItemReselectedListener(this@NavigationActivity)
        }
    }

    override fun onBackPressed() {
        findFragmentByPosition(mainPager.currentItem)?.let {
            val hasNavigatedUpNestedFragment = it.onBackPressed()

            if (!hasNavigatedUpNestedFragment) {
                handleRootLevelBackPress()
            }
        }
    }

    private fun handleRootLevelBackPress() {
        if (backStack.isNotEmpty()) {
            backStack.popOrNull()?.let(mainPager::setCurrentItem)
        } else {
            handleActivityExitAttempt()
        }
    }

    private fun handleActivityExitAttempt() {
        val now = Date().time
        val elapsed = now - lastRootLevelBackPress

        if (elapsed < BACK_PRESS_THRESHOLD_MILLIS) {
            finishAndRemoveTask()
        } else {
            Toast.makeText(
                this,
                getString(R.string.notify_press_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()
            lastRootLevelBackPress = now
        }
    }

    private fun getAllFragments() =
        supportFragmentManager.fragments
            .filter { fragment ->
                fragment is NavigationHostFragment && fragment.getTabItemId() in fragmentArguments.map { it.tabItemId }
            }.map {
                it as NavigationHostFragment
            }

    private fun getPositionByMenuItem(item: MenuItem) = fragmentArguments.indexOfFirst { it.tabItemId == item.itemId }
    private fun getMenuItemByPosition(position: Int) = bottomNavigation.menu[position]

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setViewPagerItemByMenuItem(item)
        updateBackStackByMenuItem(item)

        // This tab selection event will be captured through this hook.
        onTabSelected(item)

        return true
    }

    private fun setViewPagerItemByMenuItem(item: MenuItem) =
        setViewPagerItem(getPositionByMenuItem(item))

    private fun setViewPagerItem(position: Int) {
        with(mainPager) {
            if (currentItem != position) {
                currentItem = position
            }
        }
    }

    private fun updateBackStackByMenuItem(item: MenuItem) {
        val currentPosition = mainPager.currentItem
        val newPosition = getPositionByMenuItem(item)

        updateBackStack(currentPosition, newPosition)
    }

    private fun updateBackStack(currentPosition: Int, newPosition: Int) {
        if (currentPosition != newPosition) {
            // Back stack does not have the latest item: it stores histories.
            backStack.placeOnTop(currentPosition)
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) =
        getHostFragmentByTabItem(item)?.popToRoot() ?: Unit

    private fun getHostFragmentByTabItem(item: MenuItem) =
        findFragmentByTabItem(item)

    private fun findFragmentByTabItem(item: MenuItem) =
        findFragmentByPosition(getPositionByMenuItem(item))

    private fun findFragmentByPosition(position: Int) =
        supportFragmentManager.findFragmentByTag(
            "android:switcher:${mainPager.id}:${position}"
        ) as? NavigationHostFragment

    private fun selectTab(position: Int) {
        val menuItem = getMenuItemByPosition(position)

        selectTabByMenuItem(menuItem)
    }

    private fun selectTabByMenuItem(item: MenuItem) {
        /**
         * Setting selectedItemId behave same as tapping on an item.
         * It will trigger onNavigationItemSelected or onNavigationItemReselected.
         *
         * Event flow:
         * User selects item -> onNavigationItemSelected called -> Update BackStack & Set ViewPager.
         */
        with(bottomNavigation) {
            selectedItemId = item.itemId
        }
    }

    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment =
            NavigationHostFragment.newInstance(fragmentArguments[position])
        override fun getCount(): Int = fragmentArguments.size
    }

    companion object {
        private const val BACK_PRESS_THRESHOLD_MILLIS = 500
    }
}