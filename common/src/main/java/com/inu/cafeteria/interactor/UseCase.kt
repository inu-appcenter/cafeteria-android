/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.interactor

import android.os.Handler
import android.os.Looper
import com.inu.cafeteria.base.FailableComponent
import com.inu.cafeteria.functional.Result
import timber.log.Timber

/**
 * Abstract class for Use Case (Interactor in terms of Clean Architecture).
 * Any use case in this application should implement this.
 */
abstract class UseCase<in Params, out Type> : FailableComponent() {
    abstract fun run(params: Params): Result<Type>

    /**
     * Use thread instead of coroutine because it ruins Realm.
     */
    operator fun invoke(params: Params, onResult: (Result<Type>) -> Unit = {}) {
        Thread {
            try {
                Timber.v("UseCase running on ${Thread.currentThread().name}")
                val result = run(params)
                Handler(Looper.getMainLooper()).post { onResult(result) }
            } catch (e: Exception) {
                Timber.w("Exception inside another thread.")
                Timber.w(e)
            }
        }.start()
    }
}