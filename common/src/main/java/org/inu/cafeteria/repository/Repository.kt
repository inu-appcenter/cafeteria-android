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

    /**
     * Callback passed to repository when requesting some data.
     * Usefull when fetching data from network or DB.
     *
     * The [onSuccess] and [onFail] cannot be called at once.
     * One of them MUST be launched.
     *
     * success              -> allowed
     * fail                 -> allowed
     * success and fail     -> not allowed
     * nothing              -> not allowed
     *
     * @param async whether or not to go async. If you set true it will use enqueue, or execute.
     * @param onSuccess on successful result
     * @param onFail on failure or unexpected exception.
     */
    data class DataCallback<T>(
        val async: Boolean = true,
        val onSuccess: (T) -> Unit,
        val onFail: (Exception) -> Unit
    )
}