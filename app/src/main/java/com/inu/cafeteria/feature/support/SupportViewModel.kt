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

package com.inu.cafeteria.feature.support

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.inu.cafeteria.GlobalConfig
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.feature.support.SupportOption.Companion.availableSupportOptionsForThoseLoggedIn
import com.inu.cafeteria.feature.support.SupportOption.Companion.availableSupportOptionsForThoseNotLoggedIn
import com.inu.cafeteria.service.AccountService
import org.koin.core.inject

class SupportViewModel : BaseViewModel() {

    private val globalConfig: GlobalConfig by inject()
    private val accountService: AccountService by inject()

    private val _supportOptions = MediatorLiveData<List<SupportOption>>().apply {
        addSource(accountService.loggedInStatus()) { loggedIn ->
            if (loggedIn) {
                postValue(availableSupportOptionsForThoseLoggedIn)
            } else {
                postValue(availableSupportOptionsForThoseNotLoggedIn)
            }
        }
    }
    val supportOptions: LiveData<List<SupportOption>> = _supportOptions

    val appVersionText = mContext.getString(R.string.description_app_version, globalConfig.version)
}