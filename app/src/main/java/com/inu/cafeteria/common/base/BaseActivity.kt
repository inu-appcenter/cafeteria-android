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
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.repository.DeviceStatusRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    protected val mContext: Context by inject()
    private val deviceStatusRepository: DeviceStatusRepository by inject()

    private var networkEventHasNotEmittedSinceLastRecreation: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeNetworkStateChange(savedInstanceState != null)
        getRuntimePermissions()
    }

    // -----------------Code for network event------------------------------------------------------
    protected fun isOnline() = deviceStatusRepository.isOnline()

    private fun observeNetworkStateChange(isThisActivityRecreated: Boolean) {
        observe(deviceStatusRepository.isOnlineEvent()) {
            it?.let {
                propagateNetworkChangeOrNot(isThisActivityRecreated, it)
            }
        }
    }

    private fun propagateNetworkChangeOrNot(activityRecreated: Boolean, newNetworkState: Boolean) {
        if (activityRecreated && networkEventHasNotEmittedSinceLastRecreation) {
            Timber.i("Ignoring LiveData event on observe because this is right after Activity recreation!")
            networkEventHasNotEmittedSinceLastRecreation = false
            return
        }

        onNetworkStateChange(newNetworkState)
    }

    protected open fun onNetworkStateChange(available: Boolean) {}

    // -----------------Code for options item-------------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // -----------------Code for permissions--------------------------------------------------------
    private fun getRuntimePermissions() {
        val allNeededPermissions = requiredPermissions
            .filter { !isPermissionGranted(this, it) }
            .toTypedArray()

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, allNeededPermissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_CODE_PERMISSIONS) {
            return
        }

        if (allPermissionsGranted()) {
            onAllPermissionsGranted()
        } else {
            onPermissionNotGranted()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return requiredPermissions.all {
            isPermissionGranted(this, it)
        }
    }

    protected open val requiredPermissions: Array<String> = arrayOf()

    protected open fun onAllPermissionsGranted() {}

    protected open fun onPermissionNotGranted() {}

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}