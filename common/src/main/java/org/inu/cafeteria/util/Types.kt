package org.inu.cafeteria.util

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Types {
    companion object {
        inline fun <reified T> typeOf(): Type = object: TypeToken<T>() {}.type
    }
}