/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.usecase

import android.graphics.Bitmap
import com.inu.cafeteria.functional.Result
import com.inu.cafeteria.interactor.UseCase
import com.inu.cafeteria.util.Barcode

/**
 * Create barcode bitmap.
 */
class CreateBarcode : UseCase<String, Bitmap>() {

    override fun run(params: String) = Result.of {
        Barcode.from(params)!!
    }
}