package org.inu.cafeteria.usecase

import android.graphics.Bitmap
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.util.Barcode

/**
 * Create barcode bitmap.
 */
class CreateBarcode : UseCase<String, Bitmap>() {

    override fun run(params: String) = Result.of {
        Barcode.from(params)!!
    }
}