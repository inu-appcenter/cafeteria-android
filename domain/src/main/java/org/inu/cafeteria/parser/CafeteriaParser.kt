package org.inu.cafeteria.parser

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.inu.cafeteria.extension.tryOrNull
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.util.Types
import timber.log.Timber

class CafeteriaParser : Parser<List<Cafeteria>>() {

    /**
     * If any error on it, it will return null.
     */
    override fun parse(json: JsonElement): List<Cafeteria>? {
        return tryOrNull {
            Gson().fromJson(json, Types.typeOf<List<Cafeteria>>())
        }
    }
}