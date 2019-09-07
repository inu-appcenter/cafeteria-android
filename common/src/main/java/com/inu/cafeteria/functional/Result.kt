/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.functional

import timber.log.Timber

/**
 * A generic class that holds a value.
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    val succeeded get() = this is Success && data != null

    fun <R> onSuccess(body: (T) -> R): Result<T> {
        if (this is Success && data != null) {
            body(data)
        }
        return this
    }

    fun <R> onError(body: (Exception) -> R): Result<T> {
        if (this is Error) {
            body(exception)
        }
        return this
    }

    fun <R> either(onSuccess: (T) -> R, onError: (Exception) -> R): R =
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception)
        }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    companion object {
        fun <R> of(body: () -> R): Result<R> {
            return try {
                Success(body())
            } catch (e: Exception) {
                Timber.e(e)
                Error(e)
            }
        }
    }
}