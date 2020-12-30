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

package com.inu.cafeteria.repository

import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.entities.OrderInput
import com.inu.cafeteria.entities.WaitingOrder
import com.inu.cafeteria.extension.getOrThrow
import com.inu.cafeteria.retrofit.CafeteriaNetworkService
import com.inu.cafeteria.retrofit.scheme.AddWaitingOrderParams
import timber.log.Timber

class WaitingOrderRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
) : WaitingOrderRepository {

    // Updated by getAllWaitingOrders() call.
    private val numberOfFinishedOrders = MutableLiveData(0)

    override fun getAllWaitingOrders(deviceIdentifier: String): List<WaitingOrder> {
        // Not cached!
        val orders = networkService.getAllWaitingOrders(
            deviceIdentifier = deviceIdentifier
        ).getOrThrow() ?: return listOf()

        Timber.e("FETCH!!!")

        return orders.map {
            WaitingOrder(
                id = it.id,
                done = it.done,
                number = it.number,
                cafeteriaId = it.cafeteriaId
            )
        }.apply {
            numberOfFinishedOrders.postValue(count { it.done })
        }
    }

    override fun addWaitingOrder(orderInput: OrderInput, deviceIdentifier: String) {
        networkService.addWaitingOrder(
            AddWaitingOrderParams(
                number = orderInput.waitingNumber(),
                posNumber = orderInput.posNumber(),
                cafeteriaId = orderInput.cafeteriaId(),
                deviceIdentifier = deviceIdentifier
            )
        ).getOrThrow()
    }

    override fun deleteWaitingOrder(orderId: Int, deviceIdentifier: String) {
        networkService.deleteWaitingOrder(
            orderId = orderId,
            deviceIdentifier = deviceIdentifier
        ).getOrThrow()
    }

    override fun getNumberOfFinishedOrdersLiveData() = numberOfFinishedOrders
}