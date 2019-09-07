package com.inu.cafeteria.common.extension

import com.inu.cafeteria.R
import com.inu.cafeteria.base.Failable
import com.inu.cafeteria.exception.*
import timber.log.Timber

/**
 * Defines default data failure handle.
 * Covers all exceptions that can be thrown in repository.
 */
fun Failable.defaultDataErrorHandle(e: Exception) {
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
        is DataNotFoundException -> {
            fail(R.string.fail_no_data_for_key, show = true)
        }
        else -> {
            fail(R.string.fail_unexpected, show = true)
        }
    }

    Timber.w("Network failure: ${e::class.java.name}: ${e.message}")
}