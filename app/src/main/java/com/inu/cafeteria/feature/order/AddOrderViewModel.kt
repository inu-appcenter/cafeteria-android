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

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.onChanged
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.entities.OrderInput
import com.inu.cafeteria.exception.BadRequestException
import com.inu.cafeteria.exception.ForbiddenException
import com.inu.cafeteria.exception.NoCredentialsException
import com.inu.cafeteria.usecase.AddWaitingOrder
import com.inu.cafeteria.usecase.GetMenuSupportingCafeteriaWithoutMenus
import com.inu.cafeteria.util.SingleLiveEvent
import org.koin.core.inject
import timber.log.Timber

class AddOrderViewModel : BaseViewModel() {

    private val getCafeteria: GetMenuSupportingCafeteriaWithoutMenus by inject()
    private val addWaitingOrder: AddWaitingOrder by inject()

    private val cameraProviderLiveData = MutableLiveData<ProcessCameraProvider>()

    private val _cameraViewVisible = MutableLiveData(true)
    val cameraViewVisible: LiveData<Boolean> = _cameraViewVisible

    private val _manualViewVisible = MutableLiveData(false)
    val manualViewVisible: LiveData<Boolean> = _manualViewVisible

    private val _cafeteriaToChoose = MutableLiveData<List<CafeteriaSelectionView>>()
    val cafeteriaToChoose: LiveData<List<CafeteriaSelectionView>> = _cafeteriaToChoose

    private val _waitingNumberInputDone = MutableLiveData(false)
    val waitingNumberInputDone: LiveData<Boolean> = _waitingNumberInputDone

    val waitingNumberInput = ObservableField<String>().apply {
        onChanged {
            val numberString = get() ?: return@onChanged

            _waitingNumberInputDone.value = (numberString.length >= WAITING_NUMBER_LENGTH)
        }
    }

    val closeEvent = SingleLiveEvent<Unit>()
    val toggleFlashEvent = SingleLiveEvent<Boolean>()
    val orderSuccessfullyAddedEvent = SingleLiveEvent<Unit>()

    private var pauseHandling: Boolean = false // Will not handle when waiting for server's responses.
    private val rejectedOrderTickets = mutableListOf<OrderInput.Ticket>() // Will not handle once-rejected inputs

    fun getProcessorCameraProvider(): LiveData<ProcessCameraProvider> {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                cameraProviderLiveData.value = cameraProviderFuture.get()
            } catch (e: Exception) {
                Timber.e("Unhandled exception: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(context))

        return cameraProviderLiveData
    }

    /** Final destination */
    fun handleOrderInput(input: OrderInput) {
        if (handleIfOffline()) {
            Timber.w("Offline! Can do nothing!")
            return
        }

        if (pauseHandling) {
            Timber.v("Hey! calm down!")
            return
        }

        if (input in rejectedOrderTickets) {
            Timber.v("Oh, we don't try request with the ticket anymore: $input")
            return
        }

        pauseHandling = true // Stop until we get response.

        addWaitingOrder(input) { result ->
            result
                .onSuccess { orderSuccessfullyAddedEvent.call() }
                .onError(::handleFailure)
                .onError { if (input is OrderInput.Ticket) rejectedOrderTickets.add(input) } // To remember and not to send a request with the same ticket input again.
                .finally { pauseHandling = false }
        }
    }

    fun changeToManualInput() {
        waitingNumberInput.set("")

        setInputMode(InputMode.MODE_MANUAL)
    }

    fun changeToCameraScan() {
        setInputMode(InputMode.MODE_CAMERA)
    }

    fun close() {
        closeEvent.call()
    }

    fun toggleFlash() {
        toggleFlashEvent.value = !(toggleFlashEvent.value ?: false)
    }

    private fun setInputMode(mode: InputMode) {
        pauseHandling = false

        _cameraViewVisible.value = (mode == InputMode.MODE_CAMERA)
        _manualViewVisible.value = (mode == InputMode.MODE_MANUAL)
    }

    fun fetchCafeteriaSelectionOptions() {
        if (handleIfOffline()) {
            Timber.w("Offline! Fetch canceled.")
            return
        }

        getCafeteria(Unit) {
            it.onSuccess(::handleCafeteriaResult).onError(::handleFailure)
        }
    }

    private fun handleCafeteriaResult(cafeteria: List<Cafeteria>) {
        _cafeteriaToChoose.value = cafeteria
            .filter { it.supportNotification }
            .map {
                CafeteriaSelectionView(
                    id = it.id,
                    displayName = it.displayName ?: it.name
                )
            }
    }

    fun handleManualCafeteriaSelection(cafeteria: CafeteriaSelectionView) {
        val waitingNumber = waitingNumberInput.get()?.toInt() ?: return
        val cafeteriaId = cafeteria.id

        handleOrderInput(OrderInput.UserFriendly(waitingNumber, cafeteriaId))
    }

    private enum class InputMode {
        MODE_CAMERA,
        MODE_MANUAL
    }

    override fun handleFailure(e: Exception) {
        when (e) {
            is NoCredentialsException -> {
                handleFailure(R.string.fail_fcm_token_not_found)
            }
            is BadRequestException -> {
                handleFailure(R.string.fail_wrong_request)
            }
            is ForbiddenException -> {
                handleFailure(R.string.fail_that_order_already_exists)
            }
            else -> {
                super.handleFailure(e)
            }
        }
    }

    companion object {
        private const val WAITING_NUMBER_LENGTH = 4
    }
}