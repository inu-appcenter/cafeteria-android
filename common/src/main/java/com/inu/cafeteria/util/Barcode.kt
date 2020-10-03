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

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.inu.cafeteria.extension.tryOrNull

class Barcode {
    companion object {
        fun from(data: String, width: Int = WIDTH, height: Int = HEIGHT): Bitmap? {

            return tryOrNull {
                val byteMap = MultiFormatWriter().encode(data, BarcodeFormat.CODE_128,
                    WIDTH,
                    HEIGHT
                )
                val bitmap = Bitmap.createBitmap(
                    WIDTH,
                    HEIGHT, Bitmap.Config.ARGB_8888)

                for (i in 0 until WIDTH)

                    for (j in 0 until HEIGHT) {
                        bitmap.setPixel(i, j, if (byteMap.get(i, j)) Color.BLACK else Color.WHITE)
                    }

                bitmap
            }
        }

        private const val WIDTH = 600
        private const val HEIGHT = 360
    }
}