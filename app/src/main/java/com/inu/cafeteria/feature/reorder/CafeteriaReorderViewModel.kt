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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.extension.applyOrder
import com.inu.cafeteria.usecase.GetMenuSupportingCafeteriaWithoutMenus
import com.inu.cafeteria.usecase.GetSortingOrders
import com.inu.cafeteria.usecase.ResetCafeteriaOrder
import com.inu.cafeteria.usecase.SetCafeteriaOrder
import org.koin.core.inject
import timber.log.Timber

class CafeteriaReorderViewModel : BaseViewModel() {

    private val getCafeteria: GetMenuSupportingCafeteriaWithoutMenus by inject()
    private val getSortingOrders: GetSortingOrders by inject()
    private val setCafeteriaOrder: SetCafeteriaOrder by inject()
    private val resetCafeteriaOrder: ResetCafeteriaOrder by inject()

    private val eventHub: EventHub by inject()

    private val _cafeteria = MutableLiveData<List<CafeteriaReorderView>>()
    val cafeteria: LiveData<List<CafeteriaReorderView>> = _cafeteria

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    fun fetch() {
        if (handleIfOffline()) {
            Timber.w("Offline! Fetch canceled.")
            return
        }

        startLoading()

        getCafeteria(Unit) {
            it.onSuccess(::handleCafeteria).onError(::handleFailure)
        }
    }

    private fun startLoading() {
        _loading.value = true
    }

    private fun finishLoading() {
        _loading.value = false
    }

    fun resetOrder() {
        resetCafeteriaOrder(Unit) {
            fetch()
            eventHub.reorderEvent.call()
        }
    }

    fun onChangeOrder(orderedIds: Array<Int>) {
        setCafeteriaOrder(orderedIds) {
            eventHub.reorderEvent.call()
        }
    }

    private fun handleCafeteria(allCafeteria: List<Cafeteria>) {
        getSortingOrders(Unit) {
            it.onSuccess{ orderedIds ->
                handleCafeteriaOrdered(allCafeteria.applyOrder(orderedIds) { id })
            }.onError(::handleFailure)
        }
    }

    private fun handleCafeteriaOrdered(allCafeteriaOrdered: List<Cafeteria>) {
        val result = allCafeteriaOrdered.map { cafeteria ->
            CafeteriaReorderView(
                id = cafeteria.id,
                displayName = cafeteria.displayName ?: cafeteria.name,
            )
        }

        this._cafeteria.value = result

        finishLoading()
    }

    override fun handleFailure(e: Exception) {
        super.handleFailure(e)

        finishLoading()
    }
}