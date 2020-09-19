/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.extension

import com.inu.cafeteria.exception.NullBodyException
import com.inu.cafeteria.exception.ResponseFailException
import com.inu.cafeteria.exception.ServerNoResponseException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

fun <T> Call<T>.onSuccess(block: (Response<T>) -> Unit) {
    enqueue(object: Callback<T> {
        override fun onResponse(
            call: Call<T>,
            response: Response<T>
        ) {
            if (response.isSuccessful) {
                block(response)
            }
        }
        override fun onFailure(call: Call<T>, t: Throwable) {}
    })
}

/**
 * Wrap [enqueue] and [execute].
 * Send request and launch callback.
 * Go async? It's on your choice.
 *
 * @param async whether async or not.
 * @param onSuccess launched when successfully obtained response body from server.
 * @param onFail launched on any problem, including null response body.
 *
 * @see [Call]
 */

fun <T> Call<T>.onResult(
    async: Boolean = true,
    onSuccess: (T) -> Unit,
    onFail: (Exception) -> Unit) {

    if (async) {
        /**
         * Go async
         */

        enqueue(object: Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onSuccess(it)
                            Timber.i("Response success!")
                        }.onNull {
                            onFail(NullBodyException())
                            Timber.w("Response is success but body is null.")
                        }
                    } else {
                        onFail(ResponseFailException())
                        Timber.w("Response is fail.")
                    }

                } catch (e: Exception) {
                    onFail(e)
                    Timber.e("Unexpected exception in onResponse.")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                try {
                    onFail(ServerNoResponseException())
                    Timber.w("Server no responding.")
                } catch (e: Exception) {
                    onFail(e)
                    Timber.e("Unexpected exception in onFailure.")
                }
            }
        })
    } else {
        /**
         * Go sync
         */

        try {
            val result = execute()

            if (result.isSuccessful) {
                result.body()?.let {
                    onSuccess(it)
                    Timber.i("Response success!")
                }.onNull {
                    onFail(NullBodyException())
                    Timber.w("Response is success but body is null.")
                }
            } else {
                onFail(ResponseFailException())
                Timber.w("Response is fail.")
            }

        } catch (e: IOException) {
            Timber.e(e)
            onFail(ServerNoResponseException())
            Timber.w("Server no responding.")
        } catch (e: Exception) {
            onFail(e)
            Timber.e("Unexpected exception during synchronous execute..")
        }
    }
}

fun <T> Call<T>.getOrNull(): T? {
    try {
        val result = execute()
        return if (result.isSuccessful) {
            val body = result.body()
            body.also {
                it?.let { Timber.i("Response success!") }
                    ?.onNull { Timber.w("Response is success but body is null.") }
            }
        } else {
            Timber.w(result.errorBody()?.string())
            Timber.w("Response is fail.")
            null
        }
    } catch (e: IOException) {
        Timber.e(e)
        Timber.w("Server no responding.")
        return null
    } catch (e: Exception) {
        Timber.e("Unexpected exception during synchronous execute..")
        return null
    }
}