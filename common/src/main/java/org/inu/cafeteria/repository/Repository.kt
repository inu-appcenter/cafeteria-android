package org.inu.cafeteria.repository

import org.inu.cafeteria.base.FailableComponent
import org.inu.cafeteria.base.Startable
import timber.log.Timber

/**
 * Base class of Repository defining default action of
 * initialization and error handling.
 */
abstract class Repository : FailableComponent(), Startable {
    override fun start() {
        Timber.v("${this::class.java.name} started.")
    }

    data class DataCallback<T>(
        val onSuccess: (T) -> Unit,
        val onFail: (Exception) -> Unit
    )
}