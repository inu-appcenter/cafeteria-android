/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.model

/**
 * Represents a barcode loading state.
 */
data class BarcodeState(
    /**
     * Logged in as a student?
     */
    val isLoggedIn: Boolean = false,

    /**
     * Is the barcode loading?
     */
    val isLoading: Boolean = false,

    /**
     * Is the network or the server down?
     */
    val isNetworkDown: Boolean = false
)