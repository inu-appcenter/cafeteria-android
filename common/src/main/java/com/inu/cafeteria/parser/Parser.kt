/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.parser

import com.inu.cafeteria.base.FailableComponent

/**
 * Abstract class for object parser.
 * It could be a json parser, or anything.
 */
abstract class Parser<P, T> : FailableComponent() {
    abstract fun parse(raw: P, params: Any? = null): T?
}