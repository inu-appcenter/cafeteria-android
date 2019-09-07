/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import com.inu.cafeteria.extension.onResult
import com.inu.cafeteria.model.scheme.LoginParams
import com.inu.cafeteria.model.scheme.LoginResult
import com.inu.cafeteria.model.scheme.LogoutParams
import com.inu.cafeteria.model.scheme.LogoutResult
import com.inu.cafeteria.service.CafeteriaNetworkService
import timber.log.Timber

class LoginRepositoryImpl(
    private val networkService: CafeteriaNetworkService
) : LoginRepository() {

    private var login = false

    override fun isLoggedIn(): Boolean {
        return login
    }

    override fun login(params: LoginParams, callback: DataCallback<LoginResult>) {
        networkService.getLoginResult(params).onResult(
            async = callback.async,
            onSuccess = {
                setLoginIn(true)
                callback.onSuccess(it)
            },
            onFail = callback.onFail
        )
    }

    override fun logout(params: LogoutParams, callback: DataCallback<LogoutResult>) {
        networkService.getLogoutResult(params).onResult(
            async = callback.async,
            onSuccess = {
                setLoginIn(false)
                callback.onSuccess(it)
            },
            onFail = callback.onFail
        )
    }

    private fun setLoginIn(isLoggedIn: Boolean) {
        login = isLoggedIn
        Timber.i("Login state update. Logged ${if (isLoggedIn) "in" else "out"}.")
    }
}