package org.inu.cafeteria.parser

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.util.Types

class CafeteriaParser : Parser<List<Cafeteria>>() {

    override fun parse(json: JsonElement): List<Cafeteria> {
        return Gson().fromJson(json, Types.typeOf<List<Cafeteria>>())
    }
}