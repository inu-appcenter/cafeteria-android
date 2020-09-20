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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp

/**
 * A base Fragment that will be a holder of each page of bottom navigation.
 */
class NavigationHostFragment : Fragment() {
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    private var tabItemId: Int = -1
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseArguments()
    }

    private fun parseArguments() {
        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT)
            toolbarId = it.getInt(KEY_TOOLBAR)
            navHostId = it.getInt(KEY_NAV_HOST)
            tabItemId = it.getInt(KEY_TAB_ITEM)
            appBarConfig = it.getIntArray(KEY_ROOT_DEST)?.let { destinations ->
                AppBarConfiguration(destinations.toSet())
            } ?: return
        } ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onStart() {
        super.onStart()

        setUpToolbarAndNavController()
    }

    private fun setUpToolbarAndNavController() =
        NavigationUI.setupWithNavController(getToolbar(), getNavController(), appBarConfig)

    private fun getToolbar(): Toolbar = requireActivity().findViewById(toolbarId)

    private fun getNavController(): NavController = requireActivity().findNavController(navHostId)

    fun onBackPressed(): Boolean {
        return getNavController().navigateUp(appBarConfig)
    }

    fun popToRoot() {
        getNavController().let {
            it.popBackStack(it.graph.startDestination, false)
        }
    }

    fun handleDeepLink(intent: Intent) = requireActivity().findNavController(navHostId).handleDeepLink(intent)

    fun getTabItemId(): Int {
        return tabItemId
    }

    data class Arguments(
        val layoutRes: Int,
        val toolbarId: Int,
        val navHostId: Int,
        val tabItemId: Int,
        val rootDests: IntArray
    )

    companion object {
        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_TOOLBAR = "toolbar_key"
        private const val KEY_NAV_HOST = "nav_host_key"
        private const val KEY_TAB_ITEM = "tab_item_key"
        private const val KEY_ROOT_DEST = "root_dest_key"

        fun createArguments(layoutRes: Int,
                            toolbarId: Int,
                            navHostId: Int,
                            tabItemId: Int,
                            rootDests: IntArray) =
            Arguments(
                layoutRes,
                toolbarId,
                navHostId,
                tabItemId,
                rootDests
            )

        fun newInstance(args: Arguments) =
            NavigationHostFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_LAYOUT, args.layoutRes)
                    putInt(KEY_TOOLBAR, args.toolbarId)
                    putInt(KEY_NAV_HOST, args.navHostId)
                    putInt(KEY_TAB_ITEM, args.tabItemId)
                    putIntArray(KEY_ROOT_DEST, args.rootDests)
                }
            }
    }
}