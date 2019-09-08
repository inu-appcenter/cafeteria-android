/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.barcode

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.common.extension.getBrightness
import com.inu.cafeteria.common.extension.setBrightness
import com.inu.cafeteria.exception.ServerNoResponseException
import com.inu.cafeteria.model.BarcodeState
import com.inu.cafeteria.model.scheme.ActivateBarcodeParams
import com.inu.cafeteria.repository.LoginRepository
import com.inu.cafeteria.repository.StudentInfoRepository
import com.inu.cafeteria.usecase.ActivateBarcode
import com.inu.cafeteria.usecase.CreateBarcode
import org.koin.core.inject
import timber.log.Timber

class BarcodeViewModel : BaseViewModel() {

    private val activateBarcode: ActivateBarcode by inject()
    private val createBarcode: CreateBarcode by inject()

    private val studentInfoRepo: StudentInfoRepository by inject()
    private val loginRepo: LoginRepository by inject()

    private val _barcodeState = MutableLiveData<BarcodeState>()
    val barcodeState: LiveData<BarcodeState> = _barcodeState

    private val _studentId = MutableLiveData<String>()
    val studentId = _studentId

    private var brightness = -1

    init {
        failables += this
    }

    /**
     * Try to activated barcode.
     *
     * When called it first checks if the user is logged in.
     * Without being logged in, no callbacks are lunched and
     * immediately returned.
     * And check If the current barcode data is valid.
     * If not, it calls [onNoBarcode].
     * After that send a request to the server for activation.
     * If the request is succeeded, it runs the usecase [CreateBarcode]
     * to create a bitmap and then post the result to the [onSuccess].
     * If anything went wrong, it calls [onFail].
     * The failure could be due to either server communication or the bitmap creation.
     *
     * @param onSuccess all is well.
     * @param onFail any failure.
     * @param onNoBarcode no valid barcode found in local.
     */
    fun tryActivatingBarcode(
        onSuccess: (Bitmap?) -> Unit,
        onFail: (e: Exception) -> Unit,
        onNoBarcode: () -> Unit  // No barcode
    ) {
        studentId.value = studentInfoRepo.getStudentId()

        val barcode = studentInfoRepo.getBarcode()
        if (barcode == null) {
            _barcodeState.value = BarcodeState()
            onNoBarcode()

            Timber.w("No barcode.")

            return
        }

        val initialState = BarcodeState(
            isLoggedIn = loginRepo.isLoggedIn(),
            isLoading = true,
            isNetworkDown = false
        )

        _barcodeState.value = initialState

        // In fact it is not possible to access this feature
        // without login because the button for this feature
        // won't be shown.
        // But there could be some cases in the future where
        // this screen must be shown to the non-logged-in user.
        if (loginRepo.isLoggedIn()) {
            activateBarcode(ActivateBarcodeParams.ofActivating(barcode)) { result ->
                result.onSuccess {
                    // Here, activation is succeeded.
                    // Now we have to create a bitmap from the barcode.
                    // It takes time so has to be done in background.
                    // If succeeded, call onSuccess to set the image to the view.
                    createBarcode(barcode) {
                        it.onSuccess { bitmap ->
                            _barcodeState.value = initialState.copy(isLoading = false)
                            onSuccess(bitmap)
                            Timber.i("Barcode successfully activated.")
                        }.onError { e ->
                            onFail(e)
                            Timber.i("Barcode activation succeeded but failed to create bitmap.")
                        }
                    }
                }.onError {
                    onFail(it)
                    Timber.w("Barcode activate failed.")
                }
            }
        } else {
            // This does not happen today.
            Timber.i("Tried to load barcode but not logged in.")
        }
    }

    /**
     * Try invalidate barcode.
     *
     * If no barcode is available, it calls [onNoBarcode].
     * On failure during server communication, [onFail]
     * is called.
     *
     * @param onFail any Failure.
     * @param onNoBarcode when no barcode available in local.
     */
    fun tryInvalidatingBarcode(
        onFail: (e: Exception) -> Unit,
        onNoBarcode: () -> Unit  // No barcode
    ) {
        val barcode = studentInfoRepo.getBarcode()
        if (barcode == null) {
            _barcodeState.value = BarcodeState()
            onNoBarcode()

            Timber.w("No barcode.")

            return
        }

        if (loginRepo.isLoggedIn()) {
            activateBarcode(ActivateBarcodeParams.ofInvalidating(barcode)) { result ->
                result.onError(onFail)
            }
        }
    }

    /**
     * Barcode activation specific error handling.
     */
    fun handleActivateBarcodeFailure(e: Exception) {
        when (e) {
            is ServerNoResponseException -> _barcodeState.value = barcodeState.value?.copy(isNetworkDown = true)
            else -> defaultDataErrorHandle(e)
        }
    }

    /**
     * Make it bright!
     * @param brightness 0 ~ 255
     */
    fun brightenScreen(context: Context) {
        brightness = context.getBrightness()
        context.setBrightness(255)
    }

    /**
     * Return it back!
     */
    fun restoreScreen(context: Context) {
        // Do it only after the screen has been brightened.
        brightness.takeIf { it != -1 }?.let {
            context.setBrightness(it)
        }
    }

}