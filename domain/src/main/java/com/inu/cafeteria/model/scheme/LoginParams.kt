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

package com.inu.cafeteria.model.scheme

/**
 * Login request scheme.
 */

data class LoginParams(
    val sno: String,        // Student number
    val pw: String,         // Password
    val auto: String,       // Request token or not. Must be "1" or "0".
    val token: String,      // Token for auto login, if exists.
    val device: String      // Device platform. "android" or "ios"(?)
) {
    companion object {
        private const val AUTO_TRUE = "1"
        private const val AUTO_FALSE = "0"
        private const val DEVICE_ANDROID = "android"
        private const val EMPTY = ""

        fun ofFirstLogin(id: String, pw: String, save: Boolean): LoginParams {
            return LoginParams(
                sno = id,
                pw = pw,
                auto = if (save) AUTO_TRUE else AUTO_FALSE,
                token = EMPTY,
                device = DEVICE_ANDROID
            )
        }

        fun ofUsingToken(token: String): LoginParams {
            return LoginParams(
                sno = EMPTY,
                pw = EMPTY,
                auto = AUTO_TRUE,
                token = token,
                device = DEVICE_ANDROID
            )
        }
    }
}