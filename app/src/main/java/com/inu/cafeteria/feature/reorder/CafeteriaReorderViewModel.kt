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

package com.inu.cafeteria.feature.reorder

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.usecase.GetCafeteriaOnly
import org.koin.core.inject

class CafeteriaReorderViewModel : BaseViewModel() {

    private val getCafeteriaOnly: GetCafeteriaOnly by inject()

    private val _cafeteria = MutableLiveData<List<CafeteriaReorderView>>()
    val cafeteria: LiveData<List<CafeteriaReorderView>> = _cafeteria

    fun fetch() {
        getCafeteriaOnly(Unit) {
            it.onSuccess(::handleCafeteria).onError(::handleFailure)
        }
    }

    private fun handleCafeteria(allCafeteria: List<Cafeteria>) {
        val result = allCafeteria.map { cafeteria ->
            CafeteriaReorderView(
                id = cafeteria.id,
                displayName = cafeteria.displayName ?: cafeteria.name,
            )
        }

        this._cafeteria.value = result
    }

    private fun handleFailure(e: Exception) {
        Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
    }
}