package org.inu.cafeteria.feature.main

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.common.base.BaseActivity
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.common.extension.defaultNetworkErrorHandle
import org.inu.cafeteria.common.util.Barcode
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.model.BarcodeState
import org.inu.cafeteria.model.scheme.ActivateBarcodeParams
import org.inu.cafeteria.model.scheme.LogoutParams
import org.inu.cafeteria.repository.LoginRepository
import org.inu.cafeteria.repository.StudentInfoRepository
import org.inu.cafeteria.usecase.ActivateBarcode
import org.inu.cafeteria.usecase.Logout
import org.koin.core.inject
import timber.log.Timber

class MainViewModel : BaseViewModel() {
    private val activateBarcode: ActivateBarcode by inject()
    private val logout: Logout by inject()

    private val navigator: Navigator by inject()

    private val loginRepo: LoginRepository by inject()
    private val studentInfoRepo: StudentInfoRepository by inject()

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
            isAvailable = loginRepo.isLoggedIn(),
            isLoading = true,
            isNetworkDown = false
        )

        barcodeState.value = initialState

        if (loginRepo.isLoggedIn()) {
            activateBarcode(ActivateBarcodeParams(barcode, ActivateBarcodeParams.ACTIVATE_TRUE)) { result ->
                result.onSuccess {
                    barcodeState.value = initialState.copy(isLoading = false)
                    onSuccess(Barcode.from(barcode))
                    Timber.i("Barcode successfully activated.")
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
            activateBarcode(
                ActivateBarcodeParams(
                    barcode,
                    ActivateBarcodeParams.ACTIVATE_FALSE
                )
            ) { result ->
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
            is ServerNoResponseException -> barcodeState.value = BarcodeState(isNetworkDown = true)
            else -> defaultNetworkErrorHandle(e)
        }
    }

    fun showLogin(activity: BaseActivity) {
        navigator.showLogin()
        activity.finish()
    }
}