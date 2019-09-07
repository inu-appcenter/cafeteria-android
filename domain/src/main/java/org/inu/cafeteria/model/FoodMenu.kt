package org.inu.cafeteria.model

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