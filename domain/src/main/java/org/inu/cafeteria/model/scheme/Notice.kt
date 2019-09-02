package org.inu.cafeteria.model.scheme

/**
 * Scheme for notice. (This is single.)
 */
data class Notice(
    val all: All,
    val android: Android
) {
    data class All(val id: String, val title: String, val message: String)
    data class Android(val id: String, val title: String, val message: String)
}