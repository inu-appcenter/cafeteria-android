/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.common.base

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.base.Failable
import com.inu.cafeteria.base.FailableContainer
import com.inu.cafeteria.base.FailableHandler
import com.inu.cafeteria.base.Startable
import com.inu.cafeteria.common.extension.notify
import com.inu.cafeteria.common.extension.observe
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Base Fragment that has options menu and failure handling.
 */
abstract class BaseFragment : Fragment(), Startable, Failable, FailableContainer, FailableHandler, KoinComponent {
    private val mContext: Context by inject()

    open val optionMenuId: Int? = null

    private var menu: Menu? = null
    fun getOptionsMenu(): Menu? = menu

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(optionMenuId != null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observing failables in container is auto.
        // We can add failables in init.
        // Adding itself is recommended.
        startObservingFailables(failables)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        optionMenuId?.let {
            inflater.inflate(it, menu)
            Timber.d("Inflate option menu")
        }

        /*
        context?.let {
            menu.setTint(it, it.resolveThemeColor(R.attr.tintPrimary))
        }
        */

        this.menu = menu
    }

    override fun onDestroy() {
        super.onDestroy()

        stopObservingFailables()
    }

    /******************************
     * AS A Startable
     ******************************/
    @CallSuper override fun start() {
        Timber.i("${this::class.java.name} started.")
    }

    /******************************
     * AS A Failable
     ******************************/
    private val failure = MutableLiveData<Failable.Failure>()
    override fun getFailure(): LiveData<Failable.Failure> = failure
    override fun setFailure(failure: Failable.Failure) {
        this.failure.postValue(failure)
        Timber.w("Failure is set: ${failure.message}")
    }
    override fun fail(@StringRes message: Int, vararg formatArgs: Any?, show: Boolean) {
        setFailure(Failable.Failure(mContext.getString(message, *formatArgs), show))
    }

    /******************************
     * AS A FailableContainer
     ******************************/
    override val failables: MutableList<Failable> = mutableListOf()

    /******************************
     * AS A FailableHandler
     ******************************/
    override val observedFailables: MutableList<Failable> = mutableListOf()
    @CallSuper override fun onFail(failure: Failable.Failure) {
        if (failure.show) {
            notify(failure.message, long = true)
        }

        Timber.w("Failure with message: $failure")
    }
    final override fun startObservingFailables(failables: List<Failable>) {
        failables.forEach {
            // remove before observe to prevent double observing.
            it.getFailure().removeObservers(this)
            observe(it.getFailure()) { failure -> failure?.let(::onFail) }
            observedFailables.add(it)
        }

        Timber.i("Started observing of ${failables.joinToString{ it::class.java.name }}.")
    }
    final override fun stopObservingFailables() {
        observedFailables.forEach {
            it.getFailure().removeObservers(this)
        }
        observedFailables.clear()

        Timber.i("Stopped observing of ${observedFailables.joinToString{ it::class.java.name }}.")
    }
}