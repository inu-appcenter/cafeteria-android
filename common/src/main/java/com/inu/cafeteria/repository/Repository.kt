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

package com.inu.cafeteria.repository

import timber.log.Timber

/**
 * Base class of Repository defining default action of
 * initialization and error handling.
 */

abstract class Repository {
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