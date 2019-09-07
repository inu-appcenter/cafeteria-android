/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.util

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Types {
    companion object {
        inline fun <reified T> typeOf(): Type = object: TypeToken<T>() {}.type
        inline fun <reified T> checkType(obj: Any?, contract: T): Boolean {
            return obj != null && obj is T
        }
    }
}