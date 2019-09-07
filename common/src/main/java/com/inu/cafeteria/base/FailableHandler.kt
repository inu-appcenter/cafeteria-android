/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.base

interface FailableHandler {

    val observedFailables: MutableList<Failable>

    /**
     * What to do when some failure occur
     */
    fun onFail(failure: Failable.Failure)

    /**
     * Add failables to manage.
     */
    fun startObservingFailables(failables: List<Failable>)

    /**
     * Stop managing failables.
     * This clears observedFailables.
     */
    fun stopObservingFailables()
}