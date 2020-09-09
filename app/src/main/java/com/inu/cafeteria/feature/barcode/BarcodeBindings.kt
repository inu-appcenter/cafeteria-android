/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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