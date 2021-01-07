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

package com.inu.cafeteria.common.widget

import android.os.Bundle
import java.util.*

/**
 * A stack without duplication.
 * Useful when implementing a navigation back stack.
 *
 * IMPORTANT: back stack does not contain the current container,
 * and has no duplicated members.
 * For example: when tab moves 3 2 1 4 3 2 3 0 3 2 0 1 4 2:
 * the stack will look like: 3 2 0 1 4 (and current 2).
 */
class BackStack<T> {

    private val backStack = Stack<T>()

    fun saveBackStack(outState: Bundle) {
        outState.putSerializable("BACK_STACK", backStack)
    }

    fun restoreBackStack(savedInstanceState: Bundle) {
        @Suppress("UNCHECKED_CAST")
        val restored = (savedInstanceState.getSerializable("BACK_STACK") as? Stack<T>) ?: return

        backStack.clear()
        backStack.addAll(restored)
    }

    fun isEmpty() = backStack.isEmpty()

    fun isNotEmpty() = backStack.isNotEmpty()

    fun popOrNull() = if (backStack.size > 0) backStack.pop() else null

    /**
     * Push an item to the top.
     * If the item already exists, it will behave as if it is moved to the top.
     */
    fun placeOnTop(item: T) {
        backStack.removeIf { it == item }
        backStack.push(item)
    }
}