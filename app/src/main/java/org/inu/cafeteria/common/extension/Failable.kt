package org.inu.cafeteria.common.extension

import com.google.gson.JsonParseException
import org.inu.cafeteria.R
import org.inu.cafeteria.base.Failable
import org.inu.cafeteria.exception.BodyParseException
import org.inu.cafeteria.exception.NullBodyException
import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import timber.log.Timber
import java.io.IOException

/**
 * Defines default retrofit failure handle routine.
 */
fun Failable.defaultNetworkErrorHandle(e: Exception) {
    when (e) {
        is ServerNoResponseException -> {
            fail(R.string.fail_server, show = true)
        }
        is ResponseFailException -> {
            fail(R.string.fail_response, show = true)
        }
        is NullBodyException -> {
            fail(R.string.fail_response_body_null, show = true)
        }
        is BodyParseException -> {
            fail(R.string.fail_body_parse, show = true)
        }
        else -> {
            fail(R.string.fail_unexpected, show = true)
        }
    }

    Timber.w("Retrofit failure: ${e::class.java.name}: ${e.message}")
}