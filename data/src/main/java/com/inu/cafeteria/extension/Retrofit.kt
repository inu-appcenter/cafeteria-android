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
            onFail(ServerNoResponseException())
            Timber.w("Server no responding.")
        } catch (e: Exception) {
            onFail(e)
            Timber.e("Unexpected exception during synchronous execute..")
        }
    }
}