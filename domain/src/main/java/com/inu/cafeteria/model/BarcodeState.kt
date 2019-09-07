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