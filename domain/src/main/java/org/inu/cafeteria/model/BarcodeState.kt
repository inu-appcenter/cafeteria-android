package org.inu.cafeteria.model

data class BarcodeState(
    val isLoggedIn: Boolean = false,    // Logged in as a student?
    val isLoading: Boolean = false,     // Is barcode loading?
    val isNetworkDown: Boolean = false) // Is network or server down?