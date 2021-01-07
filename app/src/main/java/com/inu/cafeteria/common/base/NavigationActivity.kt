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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.widget.NonSwipingViewPager
import java.util.*

/**
 * A base Activity that acts as a host of bottom navigation.
 */
abstract class NavigationActivity : BaseActivity(),
    ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * overall back stack of containers
     * IMPORTANT: back stack does not contain the current container,
     * and has no duplicated members.
     * For example: when tab moves 3 2 1 4 3 2 3 0 3 2 0 1 4 2:
     * the stack will look like: 3 2 0 1 4 (and current 2).
     */
    private val backStack = Stack<Int>()

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
    protected fun jumpToTab(@IdRes tabItemId: Int): Boolean {
        val menuItem = bottomNavigation.menu.findItem(tabItemId)

        return setItem(menuItem)
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
        outState.putSerializable("BACK_STACK", backStack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val restored = (savedInstanceState.getSerializable("BACK_STACK") as? Stack<Int>) ?: return

        backStack.addAll(restored)
    }

    private fun initViewPager(savedInstanceState: Bundle?) {
        mainPager = findViewById(mainPagerRes)

        with(mainPager) {
            addOnPageChangeListener(this@NavigationActivity)
            adapter = ViewPagerAdapter()
            savedInstanceState ?: post(::checkDeepLink)
            offscreenPageLimit = fragmentArguments.size
        }
    }

    private fun checkDeepLink() {
        getAllFragments().forEachIndexed { index, fragment ->
            val hasDeepLink = fragment.handleDeepLink(intent)
            if (hasDeepLink) {
                setItem(index)
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
        if (backStack.size > 0) {
            mainPager.currentItem = backStack.pop()
        } else {
            // super.onBackPressed()
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
                fragment is NavigationHostFragment &&
                        fragment.getTabItemId() in fragmentArguments.map { it.tabItemId }
            }.map {
                it as NavigationHostFragment
            }

    private fun findFragmentByPosition(position: Int) =
        supportFragmentManager.findFragmentByTag(
            "android:switcher:${mainPager.id}:${position}"
        ) as? NavigationHostFragment

    private fun findFragmentByTabItem(item: MenuItem) =
        findFragmentByPosition(getPositionByTabItem(item))

    private fun getPositionByTabItem(item: MenuItem) =
        fragmentArguments.indexOf(fragmentArguments.find { it.tabItemId == item.itemId })

    private fun getTabItemIdByPosition(position: Int) =
        fragmentArguments[position].tabItemId

    /********************************
     * ViewPager
     ********************************/
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) = markTabSelected(position)

    /********************************
     * BottomNavigationView
     ********************************/
    override fun onNavigationItemReselected(item: MenuItem) =
        getHostFragmentByTabItem(item)?.popToRoot() ?: Unit

    override fun onNavigationItemSelected(item: MenuItem): Boolean =
        setItem(item).apply {
            // This tab selection event will be captured through this hook.
            onTabSelected(item)
        }

    private fun getHostFragmentByTabItem(item: MenuItem) =
        findFragmentByTabItem(item)

    private fun getFragmentPositionByTabItem(item: MenuItem) =
        getPositionByTabItem(item)

    private fun setItem(item: MenuItem): Boolean =
        setItem(getFragmentPositionByTabItem(item))

    private fun setItem(position: Int): Boolean {
        with(mainPager) {
            if (currentItem != position) {
                // Prevent duplication in back stack.
                backStack.removeIf { it == currentItem }
                backStack.push(currentItem)

                currentItem = position
            }
        }
        return true
    }

    private fun markTabSelected(position: Int) {
        val tabItemId = getTabItemIdByPosition(position)

        with(bottomNavigation) {

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