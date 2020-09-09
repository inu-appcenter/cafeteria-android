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

package com.inu.cafeteria.parser

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.inu.cafeteria.extension.tryOrNull
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.util.Types

class CafeteriaParser : Parser<JsonElement, List<Cafeteria>>() {

    /**
     * If any error on it, it will return null.
     */

override fun parse(raw: JsonElement, params: Any?): List<Cafeteria>? {
        return tryOrNull {
            // The Cafeteria scheme equals the server scheme.
            // Just parse it using GSON.
            Gson().fromJson<List<Cafeteria>>(raw, Types.typeOf<List<Cafeteria>>())
                .sortedBy { it.order }
        }
    }
}