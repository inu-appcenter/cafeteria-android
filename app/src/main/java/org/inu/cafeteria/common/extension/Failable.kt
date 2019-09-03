package org.inu.cafeteria.common.extension

import org.inu.cafeteria.R
import org.inu.cafeteria.base.Failable
import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import timber.log.Timber
import java.io.IOException

/**
 * Defines default retrofit failure handle routine.
 */
fun Failable.handleRetrofitException(e: Exception) {
    when (e) {
        is ServerNoResponseException -> {
            fail(R.string.fail_server, show = true)
        }
        is ResponseFailException -> {
            fail(R.string.fail_response, show = true)
        }
        else -> {
            fail(R.string.fail_unexpected, show = true)
        }
    }

    Timber.w("Retrofit failure: ${e::class.java.name}: ${e.message}")
}