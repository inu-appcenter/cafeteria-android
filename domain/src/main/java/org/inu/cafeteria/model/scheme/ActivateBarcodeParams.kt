package org.inu.cafeteria.model.scheme

/**
 * Scheme for activate barcode result.
 */
data class ActivateBarcodeParams(
    val barcode: String,    // The barcode to deal with
    val activate: String    // Activate or not. Must be "1" or "0".
) {
    companion object {
        const val ACTIVATE_TRUE = "1"
        const val ACTIVATE_FALSE = "0"

        fun ofActivating(barcode: String): ActivateBarcodeParams {
            return ActivateBarcodeParams(
                barcode = barcode,
                activate = ACTIVATE_TRUE
            )
        }

        fun ofInvalidating(barcode: String): ActivateBarcodeParams {
            return ActivateBarcodeParams(
                barcode = barcode,
                activate = ACTIVATE_FALSE
            )
        }
    }
}