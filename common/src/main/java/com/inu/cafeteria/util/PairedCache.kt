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

class PairedCache<K, V> {

    private val caches = mutableMapOf<K, Cache<V>>()

    fun isValid(key: K): Boolean {
        return caches[key]?.isValid ?: false
    }

    @Synchronized
    fun get(key: K): V? {
        isValid(key).takeIf { it } ?: throw IllegalStateException("Cannot access a cache in an invalid state.")

        return caches[key]?.get()
    }

    @Synchronized
    fun set(key: K, value: V) {
        (caches[key] ?: Cache()).apply {
            set(value)
            caches[key] = this
        }
    }
}