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

package com.inu.cafeteria.entities

sealed class OrderInput {

    /**
     * Directly scanned by camera
     */
    data class Ticket(
        val waitingNumber: Int,
        val posNumber: Int
    ) : OrderInput()

    /**
     * Manually entered
     */
    data class UserFriendly (
        val waitingNumber: Int,
        val cafeteriaId: Int,
    ) : OrderInput()

    fun waitingNumber(): Int {
        return when (this) {
            is Ticket -> waitingNumber
            is UserFriendly -> waitingNumber
        }
    }

    fun posNumber(): Int? {
        return when (this) {
            is Ticket -> posNumber
            is UserFriendly -> null
        }
    }

    fun cafeteriaId(): Int? {
        return when (this) {
            is Ticket -> null
            is UserFriendly -> cafeteriaId
        }
    }
}