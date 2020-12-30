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

import com.inu.cafeteria.GlobalConfig
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.entities.Notice
import com.inu.cafeteria.repository.DeviceStatusRepository
import com.inu.cafeteria.repository.InteractionRepository
import com.inu.cafeteria.service.AccountService
import com.inu.cafeteria.usecase.CheckForUpdate
import com.inu.cafeteria.usecase.DismissNotice
import com.inu.cafeteria.usecase.FetchNotifications
import com.inu.cafeteria.usecase.GetNewNotice
import org.koin.core.inject
import timber.log.Timber

class MainViewModel : BaseViewModel() {
    private val navigator: Navigator by inject()
    private val globalConfig: GlobalConfig by inject()

    private val getNewNotice: GetNewNotice by inject()
    private val dismissNotice: DismissNotice by inject()
    private val shouldIUpdate: CheckForUpdate by inject()
    private val fetchNotifications: FetchNotifications by inject()

    private val statusRepository: DeviceStatusRepository by inject()

    private val interactionRepository: InteractionRepository by inject()
    val numberOfUnreadAnswers = interactionRepository.getNumberOfUnreadAnswersLiveData()

    private val accountService: AccountService by inject()
    val loggedInStatus = accountService.loggedInStatus()

    fun load(activity: MainActivity) {
        if (!statusRepository.isOnline()) {
            Timber.d("Device is offline. Pending loading main view model.")
            return
        }

        checkNewNotice(activity)
        checkForUpdate(activity)
    }

    fun onLoggedIn() {
        if (!statusRepository.isOnline()) {
            Timber.d("Device is offline. Pending calling onLoggedIn callback in main view model.")
            return
        }

        // This is a global thing, so that it happens on MainViewModel.
        checkForNotifications()
    }

    private fun checkNewNotice(activity: MainActivity) {
        getNewNotice(Unit) {
            it.onSuccess { notice ->
                if (notice != null) {
                    Timber.i("Got new notice!")
                    showNewNoticeDialog(activity, notice)
                } else {
                    Timber.i("No new notice!")
                }
            }.onError(::handleFailure)
        }
    }

    private fun showNewNoticeDialog(activity: MainActivity, notice: Notice) {
        navigator.showNotice(activity, notice) {
            dismissNotice(notice)
        }
    }

    private fun checkForUpdate(activity: MainActivity) {
        shouldIUpdate(globalConfig.version) {
            it.onSuccess { shouldUpdate ->
                if (shouldUpdate) {
                    Timber.i("Need to update!")
                    navigator.showUpdate(activity)
                } else {
                    Timber.i("No need for update!")
                }
            }.onError(::handleFailure)
        }
    }

    private fun checkForNotifications() {
        fetchNotifications(Unit) {
            it.onError(::handleFailure)
        }
    }
}