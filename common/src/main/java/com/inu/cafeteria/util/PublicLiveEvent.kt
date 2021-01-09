/**
 * Copyright (C) 2020 WBPBP <potados99@gmail.com>
 *
 * This file is part of Preshoes (https://github.com/WBPBP).
 *
 * Preshoes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Preshoes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class PublicLiveEvent<T> : MutableLiveData<T?>() {
    private val mPendingPerObserver = mutableMapOf<Observer<in T>, AtomicBoolean>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        val pending = AtomicBoolean(false).apply {
            mPendingPerObserver[observer] = this
        }

        // Observe the internal MutableLiveData
        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T?>) {
        super.removeObserver(observer)

        mPendingPerObserver.remove(observer)
    }

    @MainThread
    override fun setValue(t: T?) {
        mPendingPerObserver.forEach { it.value.set(true) }
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}