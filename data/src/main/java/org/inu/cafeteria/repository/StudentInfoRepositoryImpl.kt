package org.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import org.inu.cafeteria.extension.onResult
import org.inu.cafeteria.model.scheme.ActivateBarcodeParams
import org.inu.cafeteria.model.scheme.ActivateBarcodeResult
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call

class StudentInfoRepositoryImpl(
    context: Context,
    private val networkService: CafeteriaNetworkService
) : StudentInfoRepository() {

    private val pref = context.getSharedPreferences(
        PREFERENCE_STUDENT_INFO,
        Activity.MODE_PRIVATE
    )

    override fun invalidate() {
        setStudentId(null)
        setBarcode(null)
        setLoginToken(null)
    }

    override fun getStudentId(): String? {
        return pref.getString(KEY_ID, EMPTY)
    }
    override fun setStudentId(id: String?) {
        pref.edit(true) {
            putString(KEY_ID, id)
        }
    }

    override fun getBarcode(): String? {
        return pref.getString(KEY_BARCODE, EMPTY)
    }
    override fun setBarcode(barcode: String?) {
        pref.edit(true) {
            putString(KEY_BARCODE, barcode)
        }
    }

    override fun getLoginToken(): String? {
        return pref.getString(KEY_TOKEN, EMPTY)

    }
    override fun setLoginToken(token: String?) {
        pref.edit(true) {
            putString(KEY_TOKEN, token)
        }
    }

    override fun activateBarcode(params: ActivateBarcodeParams, callback: DataCallback<ActivateBarcodeResult>) {
        networkService.getActivateBarcodeResult(params).onResult(
            async = callback.async,
            onSuccess = callback.onSuccess,
            onFail = callback.onFail
        )
    }

    companion object {
        private const val PREFERENCE_STUDENT_INFO = "studentInfo"

        private const val KEY_ID = "studentId"
        private const val KEY_BARCODE = "barcode"
        private const val KEY_TOKEN = "token"

        private const val EMPTY = ""
    }
}
