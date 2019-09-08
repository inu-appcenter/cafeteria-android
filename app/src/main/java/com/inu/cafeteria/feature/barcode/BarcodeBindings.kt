/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.barcode

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.inu.cafeteria.common.extension.isVisible
import com.inu.cafeteria.model.BarcodeState
import kotlinx.android.synthetic.main.barcode_fragment.view.*
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