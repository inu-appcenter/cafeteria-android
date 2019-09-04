package org.inu.cafeteria.extension

fun <T> T?.onNull(block: () ->Unit): T? {
    if (this == null) {
        block()
    }
    return this
}