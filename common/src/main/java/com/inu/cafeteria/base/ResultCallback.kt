package com.inu.cafeteria.base

interface ResultCallback {
    fun <T> onResult(result: Result<T>)
}