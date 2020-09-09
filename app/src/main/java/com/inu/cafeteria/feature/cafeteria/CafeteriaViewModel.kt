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

package com.inu.cafeteria.feature.cafeteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.repository.CafeteriaRepository
import com.inu.cafeteria.usecase.GetCafeteria
import com.inu.cafeteria.usecase.GetFoodMenu
import org.koin.core.inject

class CafeteriaViewModel : BaseViewModel() {

    private val getCafeteria: GetCafeteria by inject()
    private val getFoodMenu: GetFoodMenu by inject()

    private val cafeteriaRepo: CafeteriaRepository by inject()

    private val _cafeteria = MutableLiveData<List<Cafeteria>>()
    val cafeteria: LiveData<List<Cafeteria>> = _cafeteria

    private val _food = MutableLiveData<List<FoodMenu>>()
    val food: LiveData<List<FoodMenu>> = _food

    init {
        failables += this
    }

    fun loadAll(clearCache: Boolean = false) {
        if (clearCache) {
            cafeteriaRepo.invalidateCache()
        }

        getCafeteria(Unit) { result ->
            result.onSuccess { _cafeteria.value = it }.onError { defaultDataErrorHandle(it) }
        }

        getFoodMenu(Unit) { result ->
            result.onSuccess { _food.value = it }.onError { defaultDataErrorHandle(it) }
        }
    }
}