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

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.config.Config
import com.inu.cafeteria.feature.support.SupportOption.Companion.availableSupportOptionsForThoseHaveNotification
import com.inu.cafeteria.feature.support.SupportOption.Companion.availableSupportOptionsForThoseLoggedIn
import com.inu.cafeteria.feature.support.SupportOption.Companion.availableSupportOptionsForThoseNotLoggedIn
import com.inu.cafeteria.repository.InteractionRepository
import com.inu.cafeteria.service.AccountService
import org.koin.core.inject

class SupportViewModel : BaseViewModel() {

    private val accountService: AccountService by inject()
    private val interactionRepo: InteractionRepository by inject()

    private val navigator: Navigator by inject()

    val appVersionText = context.getString(R.string.description_app_version, Config.version)

    private val _supportOptions = MediatorLiveData<List<SupportOption>>().apply {
        addSource(accountService.loggedInStatus()) { updateSupportOptions() }
        addSource(interactionRepo.getNumberOfUnreadAnswersLiveData()) { updateSupportOptions() }
    }
    val supportOptions: LiveData<List<SupportOption>> = _supportOptions

    private fun updateSupportOptions() {
        val loggedIn = accountService.isLoggedIn();
        val hasNotification = interactionRepo.getNumberOfUnreadAnswersLiveData().value ?: 0 > 0

        _supportOptions.postValue(when {
            hasNotification -> availableSupportOptionsForThoseHaveNotification
            loggedIn -> availableSupportOptionsForThoseLoggedIn
            else -> availableSupportOptionsForThoseNotLoggedIn
        })
    }

    fun openKakaoTalk() {
        navigator.safeStartActivity(getKakaoIntent())
    }

    private fun getKakaoIntent() = Intent(Intent.ACTION_VIEW, Uri.parse(Config.kakaoPlusFriendLink))

    fun callUiCoop() {
        navigator.safeStartActivity(callUiCoopIntent())
    }

    private fun callUiCoopIntent() = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Config.uicoopPhoneNumber}"))
}