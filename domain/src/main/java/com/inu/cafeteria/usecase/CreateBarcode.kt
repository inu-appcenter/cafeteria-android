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