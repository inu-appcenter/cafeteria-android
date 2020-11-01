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

package com.inu.cafeteria.util

import kotlin.math.pow
import kotlin.math.sqrt

class Vector {

    companion object {
        fun sum(x: Double, y: Double): Double {
           return sqrt(x.pow(2.0) + y.pow(2.0))
        }

        fun sum(x: Float, y: Float): Double {
            return sum(x.toDouble(), y.toDouble())
        }

        fun sum(x: Double, y: Double, z: Double): Double {
            return sqrt(x.pow(2.0) + y.pow(2.0) + z.pow(2.0))
        }

        fun sum(x: Float, y: Float, z: Float): Double {
            return sum(x.toDouble(), y.toDouble(), z.toDouble())
        }
    }
}