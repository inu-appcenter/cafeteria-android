package org.inu.cafeteria.extension

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun <T> T?.onNull(block: () ->Unit): T? {
    if (this == null) {
        block()
    }
    return this
}