/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.ActivateBarcodeParams
import com.inu.cafeteria.model.scheme.ActivateBarcodeResult

abstract class StudentInfoRepository : Repository() {

    /**
     * Remove ALL student data.
     */
    abstract fun invalidate()

    /**
     * Remove one-time login data.
     * This does nothing if token exists.
     */
    abstract fun expire()

    abstract fun getStudentId(): String?
    abstract fun setStudentId(id: String?)

    abstract fun getBarcode(): String?
    abstract fun setBarcode(barcode: String?)

    abstract fun getLoginToken(): String?
    abstract fun setLoginToken(token: String?)

    abstract fun activateBarcode(params: ActivateBarcodeParams, callback: DataCallback<ActivateBarcodeResult>)
}