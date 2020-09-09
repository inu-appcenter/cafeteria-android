/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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