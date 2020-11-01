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
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.repository.DeviceStatusRepository
import com.inu.cafeteria.util.ShakeListener
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Base class providing navigation bar coloring.
 * By default, the color is [android.R.attr.windowBackground].
 *
 */

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    protected val mContext: Context by inject()
    private val deviceStatusRepository: DeviceStatusRepository by inject()

    open fun onNetworkStateChange(available: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeNetworkStateChange()
    }

    private fun observeNetworkStateChange() {
        observe(deviceStatusRepository.isOnlineLiveData()) {
            it?.let {
                onNetworkStateChange(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}