package com.inu.cafeteria.common.base

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.base.Failable
import com.inu.cafeteria.base.FailableContainer
import com.inu.cafeteria.base.FailableHandler
import com.inu.cafeteria.base.Startable
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.extension.resolveThemeColor
import com.inu.cafeteria.util.Notify
import timber.log.Timber

/**
 * Base class providing navigation bar coloring.
 * By default, the color is [android.R.attr.windowBackground].
 *
 * In normal scenario, Fragment does everything including failure handling.
 * The Activity is only to wrap the Fragment, (initially) having no error handlings.
 * But there could be cased where activity has some components other than fragment,
 * it became to be necessary to have the failure handling feature.
 *
 * Use this activity's error handling only for non-fragment component.
 * The Fragment will handle all failures inside it itself.
 *
 * TODO: Bubble up failures and handle it.
 */
abstract class BaseActivity : AppCompatActivity(), Startable, Failable, FailableContainer, FailableHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // We can only set light nav bar on API 27 in attrs, but we can do it in API 26 here
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        // Some devices don't let you modify android.R.attr.navigationBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = resolveThemeColor(android.R.attr.windowBackground)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        startObservingFailables(failables)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        setFailure(Failable.Failure(getString(message, *formatArgs), show))
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
            Notify(this).short(failure.message)
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