package org.inu.cafeteria.model.scheme

/**
 * Scheme for notice.
 */
data class Notice(
    val all: PlatformNotice,
    val android: PlatformNotice
) {
    data class PlatformNotice(val id: String, val title: String, val message: String)
}