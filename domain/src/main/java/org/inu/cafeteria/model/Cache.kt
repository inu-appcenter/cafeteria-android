package org.inu.cafeteria.model

class Cache<T> {
    private var cache: T? = null
    private var valid: Boolean = false

    val isValid: Boolean get() { return valid }

    fun get(): T? {
        return cache
    }

    fun set(value: T) {
        cache = value
        valid = true
    }

    fun invalidate() {
        clear()
        valid = false
    }

    private fun clear() {
        cache = null
    }
}