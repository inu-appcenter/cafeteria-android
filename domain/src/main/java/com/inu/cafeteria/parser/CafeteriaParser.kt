/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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