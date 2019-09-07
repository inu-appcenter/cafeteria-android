package com.inu.cafeteria.extension

inline fun <T> T?.onNull(block: () ->Unit): T? {
    if (this == null) {
        block()
    }
    return this
}