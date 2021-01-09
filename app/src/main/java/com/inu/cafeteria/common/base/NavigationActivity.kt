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
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inu.cafeteria.common.widget.BackStack
import com.inu.cafeteria.common.widget.NonSwipingViewPager
import java.util.*

/**
 * A base Activity that acts as a host of bottom navigation.
 *
 * Code flows follow steps below:
 *
 * When moved to another tab by user clicking on bottom navigation:
 *   1. onNavigationItemSelected() triggered.
 *   2. Save current ViewPager position to the back stack.
 *   3. Set ViewPager position to the new one(user clicked).
 *
 * When moved to another tab by pressing back button:
 *   1. onBackPressed() triggered.
 *   2. handleRootLevelBackPress() called.
 *   3. If possible, popFromBackStack() called.
 *   4. Pop from back stack and call setViewPagerItem() using the popped one.
 *   5. selectTab() called.
 *   6. onNavigationItemSelected() triggered.
 *   7. The ViewPager is already set to a new position(at step 4), so do nothing!
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

    /**
     * For children
     * Override to modify menu.
     */
    protected open fun onInflatedBottomMenu(menu: Menu) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        initViewPager(savedInstanceState)
        initBottomNavigation()
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

    private fun getAllFragments() =
        supportFragmentManager.fragments
            .filter { fragment ->
                fragment is NavigationHostFragment && fragment.getTabItemId() in fragmentArguments.map { it.tabItemId }
            }.map {
                it as NavigationHostFragment
            }

    private fun initBottomNavigation() {
        bottomNavigation = findViewById(bottomNavRes)

        with(bottomNavigation) {
            menu.clear()
            inflateMenu(menuRes)

            onInflatedBottomMenu(menu)

            setOnNavigationItemSelectedListener(this@NavigationActivity)
            setOnNavigationItemReselectedListener(this@NavigationActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        backStack.saveBackStack(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Calling super will automatically restore UI states(e.g. selected tab).
        super.onRestoreInstanceState(savedInstanceState)

        backStack.restoreBackStack(savedInstanceState)
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
            popFromBackStack()
        } else {
            finishAndRemoveTask()
        }
    }

    private fun popFromBackStack() {
        val popped = backStack.popOrNull() ?: return

        // Move the ViewPager first.
        setViewPagerItem(popped)

        // And then trigger onNavigationItemSelected.
        // We already moved the ViewPager, the listener won't do duplicated jobs.
        selectTab(popped)
    }

    /** BottomNavigation handlers */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (viewPagerAlreadyMoved(item)) {
            // Triggered by onBackPressed()
        } else {
            // Triggered by user's tap on BottomNavigation.
            saveCurrentViewPagerPositionToBackStack()
            setViewPagerItemByMenuItem(item)
        }

        onTabSelected(item)

        return true
    }

    private fun viewPagerAlreadyMoved(item: MenuItem): Boolean {
        val currentPosition = mainPager.currentItem
        val newPosition = getPositionByMenuItem(item)

        return currentPosition == newPosition
    }

    private fun saveCurrentViewPagerPositionToBackStack() {
        val currentPosition = mainPager.currentItem

        backStack.placeOnTop(currentPosition)
    }

    override fun onNavigationItemReselected(item: MenuItem) =
        getHostFragmentByTabItem(item)?.popToRoot() ?: Unit

    /** Setting BottomNavigation/ViewPager */
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

    private fun setViewPagerItem(position: Int) {
        mainPager.currentItem = position
    }

    private fun setViewPagerItemByMenuItem(item: MenuItem) =
        setViewPagerItem(getPositionByMenuItem(item))

    /** Getting position/MenuItem/Fragment */
    private fun getPositionByMenuItem(item: MenuItem) = fragmentArguments.indexOfFirst { it.tabItemId == item.itemId }
    private fun getMenuItemByPosition(position: Int) = bottomNavigation.menu[position]

    private fun getHostFragmentByTabItem(item: MenuItem) = findFragmentByTabItem(item)
    private fun findFragmentByTabItem(item: MenuItem) = findFragmentByPosition(getPositionByMenuItem(item))
    private fun findFragmentByPosition(position: Int) = supportFragmentManager
        .findFragmentByTag("android:switcher:${mainPager.id}:${position}") as? NavigationHostFragment

    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = NavigationHostFragment.newInstance(fragmentArguments[position])
        override fun getCount(): Int = fragmentArguments.size
    }
}