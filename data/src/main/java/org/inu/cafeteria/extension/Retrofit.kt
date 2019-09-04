package org.inu.cafeteria.extension

import org.inu.cafeteria.exception.NullBodyException
import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

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
 * Wrap Call.enqueue
 *
 * Asynchronously send request and launch callback.
 *
 * @see [Call]
 */
fun <T> Call<T>.onResult(
    onSuccess: (T) -> Unit,
    onFail: (Exception) -> Unit) {
    enqueue(object: Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            try {
                if (response.isSuccessful) {
                    response.body()
                        .onNull {
                            onFail(NullBodyException())
                            Timber.w("Response is success but body is null.")
                        }
                        ?.let {
                            onSuccess(it)
                            Timber.w("Response success!")
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
                Timber.w("Server no response.")
            } catch (e: Exception) {
                onFail(e)
                Timber.e("Unexpected exception in onFailure.")
            }
        }
    })
}