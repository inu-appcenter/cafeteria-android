package org.inu.cafeteria.common.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import org.inu.cafeteria.base.Failable
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

abstract class BasePagerAdapter : PagerAdapter(), Failable, KoinComponent {
    private val mContext: Context by inject()

    private val failure = MutableLiveData<Failable.Failure>()

    final override fun setFailure(failure: Failable.Failure) {
        this.failure.postValue(failure)
        Timber.w("Failure is set: ${failure.message}")
    }

    final override fun getFailure(): LiveData<Failable.Failure> {
        return failure
    }

    override fun fail(@StringRes message: Int, vararg formatArgs: Any?, show: Boolean) {
        setFailure(Failable.Failure(mContext.getString(message, *formatArgs), show))
    }
}