package org.inu.cafeteria.feature.cafeteria

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import org.inu.cafeteria.extension.onNull
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import timber.log.Timber

@BindingAdapter("cafeteria")
fun setCafeteria(pager: ViewPager, data: List<Cafeteria>?) {
    if (data == null) {
        Timber.i("Data is null.")
        return
    }

    pager.adapter = CafeteriaPagerAdapter(data)

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