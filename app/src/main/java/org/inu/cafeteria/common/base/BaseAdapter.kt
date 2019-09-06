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

            val diff = DiffUtil.calculateDiff(getDiffUtilCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
            onDatasetChanged()

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

    private val selection = mutableListOf<Long>()
    private val _selectionChanges = MutableLiveData<List<Long>>().apply { value = selection }
    val selectionChanges: LiveData<List<Long>> = _selectionChanges


    /**
     * Toggles the selected state for a particular view
     *
     * If we are currently in selection mode (we have an activated selection), then the state will
     * toggle. If we are not in selection mode, then we will only toggle if [force]
     */
    protected fun toggleSelection(id: Long, force: Boolean = true): Boolean {
        if (!force && selection.isEmpty()) return false

        when (selection.contains(id)) {
            true -> selection.remove(id)
            false -> selection.add(id)
        }

        _selectionChanges.value = selection

        return true
    }

    protected fun isSelected(id: Long): Boolean {
        return selection.contains(id)
    }

    fun clearSelection() {
        selection.clear()
        _selectionChanges.value = selection
        notifyDataSetChanged()
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

    open fun onDatasetChanged() {}

    /**
     * Allows the adapter implementation to provide a custom DiffUtil.Callback
     * If not, then the abstract implementation will be used
     */
    private fun getDiffUtilCallback(oldData: List<T>, newData: List<T>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    areItemsTheSame(oldData[oldItemPosition], newData[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    areContentsTheSame(oldData[oldItemPosition], newData[newItemPosition])

            override fun getOldListSize() = oldData.size

            override fun getNewListSize() = newData.size
        }
    }

    protected open fun areItemsTheSame(old: T, new: T): Boolean {
        return old == new
    }

    protected open fun areContentsTheSame(old: T, new: T): Boolean {
        return old == new
    }
}