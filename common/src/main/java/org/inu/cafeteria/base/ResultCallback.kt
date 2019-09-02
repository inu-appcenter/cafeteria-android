package org.inu.cafeteria.base

interface ResultCallback {
    fun <T> onResult(result: Result<T>)
}