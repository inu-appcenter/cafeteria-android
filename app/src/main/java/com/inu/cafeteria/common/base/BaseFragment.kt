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
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.repository.DeviceStatusRepository
import com.inu.cafeteria.repository.DeviceStatusRepositoryImpl
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Base Fragment that has options menu and failure handling.
 */

abstract class BaseFragment : Fragment(), KoinComponent {

    private val mContext: Context by inject()
    private val deviceStatusRepository: DeviceStatusRepository by inject()

    open val optionMenuId: Int? = null

    private var menu: Menu? = null
    fun getOptionsMenu(): Menu? = menu

    open fun onBackPressed() {}

    open fun onNetworkChange(available: Boolean) {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(optionMenuId != null)

        observe(deviceStatusRepository.isOnlineLiveData()) {
            it?.let {
                onNetworkChange(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = onCreateView(ViewCreator(this, inflater, container))
        ?: super.onCreateView(inflater, container, savedInstanceState)

    protected open fun onCreateView(viewCreator: ViewCreator): View? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        optionMenuId?.let {
            Timber.d("Inflate option menu view holder!")
            inflater.inflate(it, menu)
        }

        this.menu = menu
    }

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