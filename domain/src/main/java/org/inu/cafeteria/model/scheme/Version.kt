package org.inu.cafeteria.model.scheme

/**
 * Retrofit version result scheme
 */
data class Version(val android: Android) {
    data class Android(val latest: String)
}