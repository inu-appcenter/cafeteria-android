/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.LoginParams
import com.inu.cafeteria.model.scheme.LoginResult
import com.inu.cafeteria.model.scheme.LogoutParams
import com.inu.cafeteria.model.scheme.LogoutResult

/**
 * This app login policy:
 *
 * At the first time user logged in, save student number and token given by server.
 * If the user wants to be remembered, save the student number and token.
 *
 * When start method is called, isLoggedIn is set to false.
 * On the other hand, all student info is deleted if no token exists.
 *
 * If repository is alive, login is alive.
 */
abstract class LoginRepository : Repository() {

    abstract fun isLoggedIn(): Boolean

    abstract fun login(params: LoginParams, callback: DataCallback<LoginResult>)
    abstract fun logout(params: LogoutParams, callback: DataCallback<LogoutResult>)
}