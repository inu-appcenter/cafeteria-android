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

package com.inu.cafeteria.feature.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.injection.openSourceLicenses
import com.inu.cafeteria.injection.thisAppAuthors
import com.inu.cafeteria.injection.thisAppLicense
import com.inu.cafeteria.usecase.GetVersion
import kotlinx.android.synthetic.main.info_fragment.view.*
import org.koin.core.inject

class InfoFragment : BaseFragment() {

    private val getVersion: GetVersion by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_fragment, container, false).apply {
            initializeView(this)
        }
    }

    private fun initializeView(view: View) {

        // Display version.
        getVersion(Unit) {
            it.onSuccess { versionCompared ->
                view.current_version.text = getString(R.string.current_version, versionCompared.currentVersion)
                view.latest_version.text = getString(R.string.latest_version, versionCompared.latestVersion)
            }.onError {
                view.current_version.text = getString(R.string.current_version, BuildConfig.VERSION_NAME)
                view.latest_version.text = getString(R.string.latest_version_error)
            }
        }

        // Display app name.
        with(view.app_name) {
            text = thisAppLicense.name
        }

        // Display copyright.
        with(view.copyright) {
            text = thisAppLicense.copyright
        }

        // Display license info.
        with(view.license) {
            text = getString(
                R.string.desc_license,
                thisAppLicense.licenseName,
                thisAppLicense.sourceCodeReference,
                thisAppLicense.contact)
        }

        // Display authors of this app.
        with(view.authors) {
            adapter = AuthorsAdapter().apply {
                data = thisAppAuthors
            }
        }

        // Display open source software used in this app.
        with(view.open_sources) {
            adapter = LicensesAdapter().apply {
                data = openSourceLicenses.sortedBy { it.name }
            }
        }
    }
}