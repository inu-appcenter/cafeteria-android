package com.inu.cafeteria.model.scheme

/**
 * Login result scheme.
 */
data class LoginResult(
    val token: String,      // Newly created token
    val barcode: String     // Barcode data
)