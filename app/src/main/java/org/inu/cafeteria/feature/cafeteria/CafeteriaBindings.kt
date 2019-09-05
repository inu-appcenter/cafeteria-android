package org.inu.cafeteria.feature.cafeteria

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import org.inu.cafeteria.extension.onNull
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

@BindingAdapter("food")
fun setFood(pager: ViewPager, data: List<FoodMenu>?) {
    if (data == null) {
        Timber.i("Data is null.")
        return
    }

    // pager.adapter = FoodPagerAdapter(data)

    Timber.i("Food menu is updated.")
}