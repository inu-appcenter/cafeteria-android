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

package com.inu.cafeteria.feature.discount

import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.usecase.CreateBarcode
import org.koin.core.inject

class DiscountViewModel : BaseViewModel() {

    private val createBarcode: CreateBarcode by inject()

    private val _barcodeBitmap = MutableLiveData<Bitmap>()
    val barcodeBitmap: LiveData<Bitmap> = _barcodeBitmap

    fun load() {
        createBarcode(Triple("12345678", 600, 300)) {
            it.onSuccess(::handleBarcode).onError(::handleFailure)
        }
    }

    private fun handleBarcode(image: Bitmap) {
        _barcodeBitmap.value = image
    }
}