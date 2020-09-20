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
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.NavigationActivity
import com.inu.cafeteria.common.base.NavigationHostFragment
import com.inu.cafeteria.common.navigation.rootDestinations

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
            navHostId = R.id.nav_host_home,
            tabItemId = R.id.tab_home,
            rootDests = rootDestinations
        ),

        /** Discount */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_discount_base,
            toolbarId = R.id.toolbar_discount,
            navHostId = R.id.nav_host_discount,
            tabItemId = R.id.tab_discount,
            rootDests = rootDestinations
        )
    )

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}