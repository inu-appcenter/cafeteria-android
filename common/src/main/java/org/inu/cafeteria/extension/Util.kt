package org.inu.cafeteria.extension

import timber.log.Timber

fun <R> elapsedTimeMillis(action: () -> R): Long {
    val startTime = System.currentTimeMillis()
    action()
    val endTime = System.currentTimeMillis()

    return endTime - startTime
}

inline fun <T, R> withNonNull(receiver: T?, block: T.() -> R): R? {
    return if (receiver == null) null
    else with(receiver, block)
}
