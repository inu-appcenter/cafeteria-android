package org.inu.cafeteria.feature.main

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
        layout.alpha = if (isLoggedIn) 1.0f else 0.5f

        layout.login_feature_group.isVisible = isLoggedIn
        layout.login_notice.isVisible = !isLoggedIn

        layout.barcode_image.isVisible = isLoggedIn && !isLoading && !isNetworkDown
        layout.loading.isVisible = isLoggedIn && isLoading && !isNetworkDown
        layout.internet_warning.isVisible = isNetworkDown
    }

    Timber.i("Barcode state is set.")
}