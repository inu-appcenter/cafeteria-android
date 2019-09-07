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
 * Login result scheme.
 */
data class LoginResult(
    val token: String,      // Newly created token
    val barcode: String     // Barcode data
)