package org.inu.cafeteria.common.base

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.inu.cafeteria.base.Failable
import org.inu.cafeteria.common.extension.setVisible
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Base RecyclerView.Adapter that provides some convenience when creating a new Adapter, such as
 * data list handing and item animations
 */
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder>(), Failable, KoinComponent {
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

    var data: List<T> = ArrayList()
        set(value) {
            if (field === value) return

            field = value

            notifyDataSetChanged()

            setEmptyView(value.isEmpty())
        }

    @CallSuper
    protected open fun setEmptyView(isDataEmpty: Boolean) {
        emptyView?.setVisible(isDataEmpty)
    }

    /**
     * This view can be set, and the adapter will automatically control the visibility of this view
     * based on the data
     */
    var emptyView: View? = null
        set(value) {
            field = value
            field?.setVisible(data.isEmpty())
        }

    fun getItem(position: Int): T? {
        if (position < 0) {
            Timber.w("Trying to access index $position!!")
            return null
        }

        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}