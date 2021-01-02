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

enum class OnboardingHint(
    private val baseKey: String,
    val minimumPreExposure: Long = 5,
    val hasBeenShownKey: String = "${baseKey}_has_shown",
    val exposureCountKey: String = "${baseKey}_exposure"
) {

    /** User can sort cafeteria order on the first tab. */
    SortingCafeteria("com.inu.cafeteria.hint_sorting_cafeteria", 3),

    /** User can toggle brightness of a barcode screen on the third tab. */
    ToggleBrightness("com.inu.cafeteria.hint_toggle_brightness", 1)
}