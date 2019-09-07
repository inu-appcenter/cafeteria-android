/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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