package org.inu.cafeteria.parser

import com.google.gson.JsonElement
import org.inu.cafeteria.base.FailableComponent

/**
 * Abstract class for object parser.
 * It could be a json parser, or anything.
 */
abstract class Parser<P, T> : FailableComponent() {
    abstract fun parse(raw: P, params: Any? = null): T?
}