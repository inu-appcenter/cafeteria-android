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

import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.config.Config
import com.inu.cafeteria.entities.Notice
import com.inu.cafeteria.exception.NoCredentialsException
import com.inu.cafeteria.repository.InteractionRepository
import com.inu.cafeteria.repository.WaitingOrderRepository
import com.inu.cafeteria.service.AccountService
import com.inu.cafeteria.usecase.*
import org.koin.core.inject
import timber.log.Timber

class MainViewModel : BaseViewModel() {
    private val navigator: Navigator by inject()

    private val getNewNotice: GetNewNotice by inject()
    private val dismissNotice: DismissNotice by inject()
    private val shouldIUpdate: CheckForUpdate by inject()
    private val getWaitingOrders: GetWaitingOrders by inject()
    private val fetchUnreadAnswers: FetchUnreadAnswers by inject()

    private val interactionRepository: InteractionRepository by inject()
    val numberOfUnreadAnswers = interactionRepository.getNumberOfUnreadAnswersLiveData()

    private val waitingOrderRepository: WaitingOrderRepository by inject()
    val numberOfFinishedOrders = waitingOrderRepository.getNumberOfFinishedOrdersLiveData()

    private val accountService: AccountService by inject()
    val loggedInStatus = accountService.loggedInStatus()

    fun load(activity: MainActivity) {
        if (saySorryIfOffline()) {
            Timber.d("Device is offline. Cancel loading main view model.")
            return
        }

        checkNewNotice(activity)
        checkForUpdate(activity)

        // This is a global thing, so that it happens on MainViewModel.
        checkForFinishedOrders()
    }

    fun onLoggedIn() {
        if (saySorryIfOffline()) {
            Timber.d("Device is offline. Cancel calling onLoggedIn callback in main view model.")
            return
        }

        // This is a global thing, so that it happens on MainViewModel.
        checkForUnreadAnswers()
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
        shouldIUpdate(Config.version) {
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

    private fun checkForFinishedOrders() {
        getWaitingOrders(Unit) {
            it.onError(::handleFailure)
        }
    }

    private fun checkForUnreadAnswers() {
        fetchUnreadAnswers(Unit) {
            it.onError(::handleFailure)
        }
    }

    override fun handleFailure(e: Exception) {
        when (e) {
            is NoCredentialsException -> {
                // Do nothing.
                // It is perfectly ok to be without the firebase token
                // on the very first launch right after installation.
            }
            else -> {
                super.handleFailure(e)
            }
        }
    }
}