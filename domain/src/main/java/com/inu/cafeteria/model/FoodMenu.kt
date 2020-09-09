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

package com.inu.cafeteria.model

/**
 * Represents food menus of each corner of a single cafeteria.
 */

data class FoodMenu(
    /**
     * The root key of a cafeteria.
     */

val cafeteriaNumber: Int,

    /**
     * Collection of corners,
     * which could be A corner, B corner, A corner lunch, or B corner dinner, etc.
     */

val corners: List<Corner>
) {
    data class Corner(
        /**
         * The server wants the corners the be shown in this order.
         */

val order: Int,

        /**
         * Name of the corner.
         */

val title: String,

        /**
         * Menu represented in multiple lines.
         * In current scheme each string stores food name or price or calorie.
         *
         * TODO: Food scheme very differs by all cafeteria. Fix it in server.
         */

val menu: List<String>
    )
}