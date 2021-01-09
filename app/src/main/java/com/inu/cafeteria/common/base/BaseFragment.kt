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

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.repository.DeviceStatusRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Works best with Navigation.
 */
abstract class BaseFragment : Fragment(), KoinComponent {

    protected val mContext: Context by inject()
    private val deviceStatusRepository: DeviceStatusRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(optionMenuId != null)
        observeNetworkStateChange(savedInstanceState == null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = onCreateView(ViewCreator(this, inflater, container))
        ?: super.onCreateView(inflater, container, savedInstanceState)

    protected open fun onCreateView(viewCreator: ViewCreator): View? = null

    override fun onResume() {
        super.onResume()

        prepareToolbar()
    }

    // -----------------Code for network event------------------------------------------------------
    protected fun isOnline() = deviceStatusRepository.isOnline()

    private fun observeNetworkStateChange(isThisFirstTimeCreated: Boolean) {
        if (isThisFirstTimeCreated) {
            onNetworkStateChange(isOnline())
        }

        observe(deviceStatusRepository.networkStateChangeEvent()) {
            it?.let(::onNetworkStateChange)
        }
    }

    protected open fun onNetworkStateChange(available: Boolean) {}

    // -----------------Code for toolbar------------------------------------------------------------
    /**
     * Set given toolbar as an Activity's primary action bar.
     * Useful on single-activity-multiple-fragment situation.
     */
    private fun prepareToolbar() {
        setupToolbarForActivityAndNavController(
            activity?.findViewById(toolbarId ?: return) ?: return
        )
    }

    private fun setupToolbarForActivityAndNavController(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        if (setupToolbarForNavigation) {
            safeSetupWithNavController(toolbar)
        }
    }

    private fun safeSetupWithNavController(toolbar: Toolbar) {
        try {
            toolbar.setupWithNavController(findNavController())
        } catch (e: Exception) {
            Timber.e("Failed to setup toolbar with navigation controller: $e")
        }
    }

    open val toolbarId: Int? = null

    open val setupToolbarForNavigation: Boolean = false

    // -----------------Code for options item-------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        optionMenuId?.let {
            Timber.d("Inflate option menu!")
            inflater.inflate(it, menu)
        }
    }

    open fun onBackPressed() {}

    open val optionMenuId: Int? = null

    // -----------------Code for view---------------------------------------------------------------
    class ViewCreator(
        val fragment: BaseFragment,
        val inflater: LayoutInflater,
        val container: ViewGroup?
    ) {

        inline operator fun <reified T: ViewDataBinding> invoke(also: T.() -> Unit = {}) =
            createView(also)

        inline fun <reified T: ViewDataBinding> createView(also: T.() -> Unit = {}): View {
            val inflateMethod = T::class.java.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )

            return (inflateMethod.invoke(null, inflater, container, false) as T)
                .apply { lifecycleOwner = fragment }
                .apply { also(this) }
                .root
        }
    }
}