package org.inu.cafeteria.extension

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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