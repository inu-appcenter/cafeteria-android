package org.inu.cafeteria.feature.main

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import kotlinx.android.synthetic.main.drawer.view.*
import org.inu.cafeteria.common.extension.isVisible
import org.inu.cafeteria.model.BarcodeState
import timber.log.Timber

@BindingAdapter("barcodeState")
fun setBarcodeState(layout: ConstraintLayout, state: BarcodeState?) {
    state ?: return

    with(state) {
        layout.barcode.isVisible = isAvailable && !isLoading && !isNetworkDown
        layout.loading.isVisible = isAvailable && isLoading && !isNetworkDown
        layout.internet_warning.isVisible = isNetworkDown
    }

    Timber.i("Barcode state is set.")
}