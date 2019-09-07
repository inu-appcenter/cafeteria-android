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
 * Scheme for activated barcode result.
 */
data class ActivateBarcodeResult(
    /**
     * Is activated?
     *
     * In fact, if activation request is succeded,
     * it will have the same value as the request.
     * That is defined in the server logic.
     */
    val active: String
)