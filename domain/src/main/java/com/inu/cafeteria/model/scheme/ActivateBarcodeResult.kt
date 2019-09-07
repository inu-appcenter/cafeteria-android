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