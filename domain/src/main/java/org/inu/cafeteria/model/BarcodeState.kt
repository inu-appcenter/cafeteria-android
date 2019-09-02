package org.inu.cafeteria.model

data class BarcodeState(
    val isAvailable: Boolean,
    val isLoading: Boolean,
    val isNetworkDown: Boolean)