package com.inu.cafeteria.model

/**
 * A simple cache class for storing fetched data.
 */
class Cache<T> {
    private var cache: T? = null
    private var valid: Boolean = false

    /**
     * It means 'is this the valid projection of the server data?'
     */
    val isValid: Boolean get() { return valid }

    /**
     * Get data inside the cache.
     * When calling this method, the cache must be valid.
     * Or it will throw!
     *
     * @return the data. Could be null.
     *
     * @throws IllegalStateException
     */
    fun get(): T? {
        isValid.takeIf { it } ?: throw IllegalStateException("Cannot access a cache in an invalid state.")

        return cache
    }

    /**
     * Set the cache.
     * It accepts null, which could be seen as an intention 'empty the cache'.
     * For example, the server could give an empty data. In that case the cache
     * will also be empty(null).
     * But it is still valid. Because the cache is projecting the real data on server.
     * So Calling this method will ALWAYS set the cache as valid.
     */
    fun set(value: T?) {
        cache = value
        valid = true
    }

    /**
     * Invalidate the cache.
     */
    fun invalidate() {
        clear()
        valid = false
    }

    /**
     * Set the cache to null.
     * This method is internal only, must be called with setting [isValid].
     */
    private fun clear() {
        cache = null
    }
}