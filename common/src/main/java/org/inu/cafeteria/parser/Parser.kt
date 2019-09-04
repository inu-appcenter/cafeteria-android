package org.inu.cafeteria.parser

import com.google.gson.JsonElement
import org.inu.cafeteria.base.FailableComponent

abstract class Parser<T> : FailableComponent() {
    abstract fun parse(json: JsonElement): T?
}