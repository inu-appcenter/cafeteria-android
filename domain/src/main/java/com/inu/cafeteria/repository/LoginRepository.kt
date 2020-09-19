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

abstract class LoginRepository {

    abstract fun isLoggedIn(): Boolean

    abstract fun login(params: LoginParams, callback: Repository.DataCallback<LoginResult>)
    abstract fun logout(params: LogoutParams, callback: Repository.DataCallback<LogoutResult>)
}