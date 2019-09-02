package org.inu.cafeteria.model.scheme

/**
 * Logout request scheme.
 */
data class LogoutParams(
    val token: String   // The token we got on login
)