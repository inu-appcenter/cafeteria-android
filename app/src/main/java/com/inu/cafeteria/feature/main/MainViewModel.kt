/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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