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

import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseActivity
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.model.scheme.LogoutParams
import com.inu.cafeteria.repository.LoginRepository
import com.inu.cafeteria.repository.StudentInfoRepository
import com.inu.cafeteria.usecase.ActivateBarcode
import com.inu.cafeteria.usecase.Logout
import org.koin.core.inject

class MainViewModel : BaseViewModel() {
    private val activateBarcode: ActivateBarcode by inject()
    private val logout: Logout by inject()

    private val loginRepo: LoginRepository by inject()
    private val studentInfoRepo: StudentInfoRepository by inject()

    private val navigator: Navigator by inject()

    init {
        failables += this
        failables += activateBarcode
        failables += loginRepo
        failables += studentInfoRepo
    }

    fun tryLogout(
        onSuccess: () -> Unit,
        onFail: (e: Exception) -> Unit,
        onNoToken: () -> Unit
    ) {
        val token = studentInfoRepo.getLoginToken()
        if (token == null) {
            onNoToken()
            return
        }

        if (loginRepo.isLoggedIn()) {
            logout(LogoutParams(token)) {
                it.onSuccess { onSuccess() }.onError(onFail)
            }
        } else {
            onSuccess()
        }

        studentInfoRepo.invalidate()
    }

    fun removeUserData() {
        studentInfoRepo.invalidate()
    }

    fun showLogin(activity: BaseActivity) {
        navigator.showLogin()
        activity.finish()
    }

    fun showInfo() {
        navigator.showInfo()
    }

    fun showBarcode() {
        navigator.showBarcode()
    }
}