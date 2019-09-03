package org.inu.cafeteria.extension

fun <T> T?.onNull(block: () ->Unit): T? {
    block()
    return this
}