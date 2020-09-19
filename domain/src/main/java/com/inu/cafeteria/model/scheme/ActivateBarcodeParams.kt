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

package com.inu.cafeteria.model.scheme

/**
 * Scheme for activated barcode result.
 */

data class ActivateBarcodeParams(
    /**
     * The barcode data to activate or not.
     */

    val barcode: String,

    /**
     * Whether to activate or not.
     * Must be "1"(activate) or "0"(deactivate).
     */

    val activated: String
) {
    companion object {
        const val ACTIVATE_TRUE = "1"
        const val ACTIVATE_FALSE = "0"

        fun ofActivating(barcode: String): ActivateBarcodeParams {
            return ActivateBarcodeParams(
                barcode = barcode,
                activated = ACTIVATE_TRUE
            )
        }

        fun ofInvalidating(barcode: String): ActivateBarcodeParams {
            return ActivateBarcodeParams(
                barcode = barcode,
                activated = ACTIVATE_FALSE
            )
        }
    }
}