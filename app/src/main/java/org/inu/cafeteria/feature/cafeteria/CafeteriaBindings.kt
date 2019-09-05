package org.inu.cafeteria.feature.cafeteria

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import timber.log.Timber

@BindingAdapter("cafeteria")
fun setCafeteria(listView: RecyclerView, data: List<Cafeteria>?) {
    if (data == null) {
        Timber.i("Data is null.")
        return
    }

    val adapter = listView.adapter as? CafeteriaAdapter
    if (adapter == null) {
        Timber.w("Adapter not set.")
        return
    }

    adapter.data = data

    Timber.i("Cafeteria is updated.")
}

@BindingAdapter("corners")
fun setCorners(listView: RecyclerView, data: FoodMenu?) {
    if (data == null) {
        Timber.i("Data is null.")
        return
    }

    val adapter = listView.adapter as? CornersAdapter
    if (adapter == null) {
        Timber.w("Adapter not set.")
        return
    }

    adapter.data = data.corners

    Timber.i("Corners is(are) updated.")
}
