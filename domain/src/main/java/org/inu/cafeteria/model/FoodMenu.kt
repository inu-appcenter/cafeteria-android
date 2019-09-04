package org.inu.cafeteria.model

/**
 * Represents food menus of each corner of a single cafeteria.
 */
data class FoodMenu(
    // Root key
    val cafeteriaNumber: Int,

    // Each corner of the cafeteria combined with breakfast, lunch, dinner, or whatever...
    val corners: List<Corner>
) {
    data class Corner(
        // Starts from zero.
        val order: Int,

        val title: String,
        val menu: List<String>
    )
}