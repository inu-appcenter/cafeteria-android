package org.inu.cafeteria.parser

import com.google.gson.JsonElement
import org.inu.cafeteria.base.FailableComponent

abstract class Parser<P, T> : FailableComponent() {
    abstract fun parse(raw: P, params: Any? = null): T?
}