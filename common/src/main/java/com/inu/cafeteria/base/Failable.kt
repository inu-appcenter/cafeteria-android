/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.base

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData

/**
 * Represents a component that can fail.
 */
interface Failable {
    /**
     * Get failure Live Data.
     */
    fun getFailure(): LiveData<Failure>

    /**
     * Set failure Live Data.
     */
    fun setFailure(failure: Failure)

    /**
     * Compact.
     * Call setFailure inside it.
     */
    fun fail(@StringRes message: Int, vararg formatArgs: Any?, show: Boolean = false)

    data class Failure(val message: String, val show: Boolean = false)
}