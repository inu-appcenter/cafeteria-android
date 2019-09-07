/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.base

/**
 * A component that has Failable inside.
 */
interface FailableContainer {
    /**
     * List of failables to handle.
     */
    val failables: MutableList<Failable>
}