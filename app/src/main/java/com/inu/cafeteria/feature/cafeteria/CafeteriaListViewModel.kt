/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.cafeteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.repository.CafeteriaRepository
import com.inu.cafeteria.usecase.GetCafeteria
import org.koin.core.inject

class CafeteriaListViewModel : BaseViewModel() {

    private val getCafeteria: GetCafeteria by inject()

    private val cafeteriaRepo: CafeteriaRepository by inject()

    private val _cafeteria = MutableLiveData<List<Cafeteria>>()
    val cafeteria: LiveData<List<Cafeteria>> = _cafeteria

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
    }
}