/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.main

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseActivity
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.exception.ServerNoResponseException
import com.inu.cafeteria.model.BarcodeState
import com.inu.cafeteria.model.scheme.ActivateBarcodeParams
import com.inu.cafeteria.model.scheme.LogoutParams
import com.inu.cafeteria.repository.LoginRepository
import com.inu.cafeteria.repository.StudentInfoRepository
import com.inu.cafeteria.usecase.ActivateBarcode
import com.inu.cafeteria.usecase.CreateBarcode
import com.inu.cafeteria.usecase.Logout
import org.koin.core.inject
import timber.log.Timber

class MainViewModel : BaseViewModel() {
    private val activateBarcode: ActivateBarcode by inject()
    private val createBarcode: CreateBarcode by inject()
    private val logout: Logout by inject()

    private val navigator: Navigator by inject()

    private val loginRepo: LoginRepository by inject()
    private val studentInfoRepo: StudentInfoRepository by inject()

    private val handler = Handler(Looper.getMainLooper())

    init {
        failables += this
        failables += activateBarcode
        failables += loginRepo
        failables += studentInfoRepo
    }

    val barcodeState = MutableLiveData<BarcodeState>().apply {
        value = BarcodeState()
    }

    val studentId = MutableLiveData<String>().apply {
        value = "STUDENT ID"
    }

    /**
     * Try to activated barcode.
     */
    fun tryLoadingBarcode(
        onSuccess: (Bitmap?) -> Unit,
        onFail: (e: Exception) -> Unit,
        onNoBarcode: () -> Unit  // No barcode
    ) {
        studentId.value = studentInfoRepo.getStudentId()

        val barcode = studentInfoRepo.getBarcode()
        if (barcode == null) {
            barcodeState.value = BarcodeState()
            onNoBarcode()
            return
        }

        val initialState = BarcodeState(
            isLoggedIn = loginRepo.isLoggedIn(),
            isLoading = true,
            isNetworkDown = false
        )

        barcodeState.value = initialState

        if (loginRepo.isLoggedIn()) {
            activateBarcode(ActivateBarcodeParams.ofActivating(barcode)) { result ->
                result.onSuccess {
                    // Here, activation is succeeded.
                    // Now we have to create a bitmap from the barcode.
                    // It takes time so has to be done in background.
                    // If succeeded, call onSuccess to set the image to the view.
                    createBarcode(barcode) {
                        it.onSuccess { bitmap ->
                            barcodeState.value = initialState.copy(isLoading = false)
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
            Timber.i("Tried to load barcode but not logged in.")
        }

        Timber.i("Drawer started opening.")
    }

    /**
     * Try invalidate barcode.
     */
    fun tryInvalidateBarcode(
        onFail: (e: Exception) -> Unit,
        onNoBarcode: () -> Unit  // No barcode
    ) {
        val barcode = studentInfoRepo.getBarcode()
        if (barcode == null) {
            barcodeState.value = BarcodeState()
            onNoBarcode()
            return
        }

        if (loginRepo.isLoggedIn()) {
            activateBarcode(ActivateBarcodeParams.ofInvalidating(barcode)) { result ->
                result.onError(onFail)
            }
        }

        Timber.i("Drawer closed.")
    }

    fun tryLogout(
        onSuccess: () -> Unit,
        onFail: (e: Exception) -> Unit,
        onNoToken: () -> Unit
    ) {
        val token = studentInfoRepo.getLoginToken()
        if (token == null) {
            onNoToken()
            return
        }

        if (loginRepo.isLoggedIn()) {
            logout(LogoutParams(token)) {
                it.onSuccess { onSuccess() }.onError(onFail)
            }
        } else {
            onSuccess()
        }

        studentInfoRepo.invalidate()
    }

    /**
     * Barcode activated specific error handling.
     */
    fun handleActivateBarcodeFailure(e: Exception) {
        when (e) {
            is ServerNoResponseException -> barcodeState.value = barcodeState.value?.copy(isNetworkDown = true)
            else -> defaultDataErrorHandle(e)
        }
    }

    fun showLogin(activity: BaseActivity) {
        navigator.showLogin()
        activity.finish()
    }

    fun showInfo() {
        navigator.showInfo()
    }
}