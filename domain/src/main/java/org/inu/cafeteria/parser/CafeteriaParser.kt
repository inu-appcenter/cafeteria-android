package org.inu.cafeteria.parser

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.inu.cafeteria.extension.tryOrNull
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.util.Types
import timber.log.Timber

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