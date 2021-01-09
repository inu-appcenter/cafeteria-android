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

package com.inu.cafeteria.feature.order

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.RemoteMessage
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.entities.PerfectReviewCondition
import com.inu.cafeteria.entities.WaitingOrder
import com.inu.cafeteria.exception.BadRequestException
import com.inu.cafeteria.exception.NoCredentialsException
import com.inu.cafeteria.exception.ResourceNotFoundException
import com.inu.cafeteria.repository.AppUsageRepository
import com.inu.cafeteria.usecase.DeleteWaitingOrder
import com.inu.cafeteria.usecase.GetMenuSupportingCafeteriaWithoutMenus
import com.inu.cafeteria.usecase.GetWaitingOrders
import com.inu.cafeteria.util.SingleLiveEvent
import org.koin.core.inject
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timer

class WaitingOrderViewModel : BaseViewModel() {

    private val getCafeteria: GetMenuSupportingCafeteriaWithoutMenus by inject()
    private val getWaitingOrders: GetWaitingOrders by inject()
    private val deleteWaitingOrder: DeleteWaitingOrder by inject()

    private val navigator: Navigator by inject()

    private val appUsageRepository: AppUsageRepository by inject()

    private val _orders = MutableLiveData<List<WaitingOrderView>>()
    val orders: LiveData<List<WaitingOrderView>> = _orders

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    val shakeAddButtonEvent = SingleLiveEvent<Unit>()
    val askForReviewEvent = SingleLiveEvent<Unit>()
    val showOrderReadyDialogEvent = SingleLiveEvent<Pair<String, String>>()

    private var shakeButtonTimer: Timer? = timer(period = 3000) {
        shakeAddButtonEvent.postValue(Unit)
    }

    fun clearAllOrderNotifications() {
        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        // Warning: this will cancel notifications in other channels.
        notificationManager.cancelAll()
    }

    fun fetchWaitingOrders() {
        if (saySorryIfOffline()) {
            Timber.w("Offline! Fetch canceled.")
            return
        }

        _loading.value = true

        getWaitingOrders(Unit) {
            it
                .onSuccess(::handleWaitingOrders)
                .onError(::handleFailure)
        }
    }

    private fun handleWaitingOrders(orders: List<WaitingOrder>) {
        getCafeteria(Unit) { result ->
            result
                .onSuccess { handleWaitingOrdersWithCafeteria(orders, it) }
                .onError(::handleFailure)
                .finally { _loading.value = false }
        }
    }

    private fun handleWaitingOrdersWithCafeteria(orders: List<WaitingOrder>, cafeteria: List<Cafeteria>) {
        _orders.value = orders.map { order ->
            WaitingOrderView(
                orderId = order.id,
                done = order.done,
                waitingNumber = String.format("%04d", order.number) /*this is necessary*/,
                cafeteriaDisplayName = getCafeteriaNameById(cafeteria, order.cafeteriaId)
            )
        }
    }

    private fun getCafeteriaNameById(allCafeteria: List<Cafeteria>, cafeteriaId: Int): String {
        val cafeteriaFound = allCafeteria.find { it.id == cafeteriaId } ?: return ""

        return cafeteriaFound.displayName ?: cafeteriaFound.name
    }

    fun addWaitingOrder() {
        navigator.showAddWaitingOrder()
    }

    fun handleNotification(message: RemoteMessage) {
        fetchWaitingOrders()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate()
        }

        val title = message.notification?.title ?: mContext.getString(R.string.title_order_ready)
        val body = message.notification?.body ?: mContext.getString(R.string.description_order_ready)

        showOrderReadyDialogEvent.value = Pair(title, body)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrate() {
        val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        val effect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)

        vibrator?.vibrate(effect)
    }

    fun askForReviewIfAllOrdersFinished() {
        val unfinishedOrders = _orders.value?.filter { !it.done }?.size ?: 0
        if (unfinishedOrders > 0) {
            // We will ask for review only when order list is empty.
            return
        }

        askForReviewIfPossible()
    }

    private fun askForReviewIfPossible() {
        appUsageRepository.markReviewChanceExposed(PerfectReviewCondition.AfterAllOrdersFinished)

        val goodTimeToAskForReview = appUsageRepository.isThisPerfectTimeForReview(PerfectReviewCondition.AfterAllOrdersFinished)
        if (!goodTimeToAskForReview) {
            return
        }

        Handler().postDelayed({
            askForReviewEvent.call()
        }, 1000)
    }

    fun markAskedForReview() {
        appUsageRepository.markReviewRequestShown(PerfectReviewCondition.AfterAllOrdersFinished)
    }

    fun deleteFinishedOrders() {
        _orders.value
            ?.filter { it.done }
            ?.forEach { deleteWaitingOrder(it.orderId) }
    }

    fun deleteWaitingOrder(orderId: Int) {
        _loading.value = true

        deleteWaitingOrder(orderId) {
            it
                .onSuccess { fetchWaitingOrders()/*refresh*/ }
                .onError(::handleFailure)
                .finally { _loading.value = false }
        }
    }

    override fun handleFailure(e: Exception) {
        when (e) {
            is NoCredentialsException -> {
                handleFailure(R.string.fail_fcm_token_not_found)
            }
            is BadRequestException -> {
                handleFailure(R.string.fail_wrong_request)
            }
            is ResourceNotFoundException -> {
                handleFailure(R.string.fail_that_order_does_not_exist)
            }
            else -> {
                super.handleFailure(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        shakeButtonTimer?.cancel()
        shakeButtonTimer = null
    }
}