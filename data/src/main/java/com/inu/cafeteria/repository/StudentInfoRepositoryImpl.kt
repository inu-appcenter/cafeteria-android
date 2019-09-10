/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import com.inu.cafeteria.data.BuildConfig
import com.inu.cafeteria.extension.onResult
import com.inu.cafeteria.extension.tryOrNull
import com.inu.cafeteria.model.scheme.ActivateBarcodeParams
import com.inu.cafeteria.model.scheme.ActivateBarcodeResult
import com.inu.cafeteria.service.CafeteriaNetworkService
import com.scottyab.aescrypt.AESCrypt
import timber.log.Timber

class StudentInfoRepositoryImpl(
    context: Context,
    private val networkService: CafeteriaNetworkService
) : StudentInfoRepository() {

    private val pref = context.getSharedPreferences(
        PREFERENCE_STUDENT_INFO,
        Activity.MODE_PRIVATE
    )

    init {
        // This is executed for one time in its lifecycle.
        // Uncleared student data should be purged early but for one time only.
        expire()
    }

    override fun invalidate() {
        setStudentId(null)
        setBarcode(null)
        setLoginToken(null)

        Timber.i("Invalidated All user data.")
    }

    override fun expire() {
        if (getLoginToken().isNullOrEmpty()) {
            // Remove one-time login data.
            invalidate()
            Timber.i("Purged all user data.")
        }
    }

    override fun getStudentId(): String? {
        return getString(KEY_ID)
    }
    override fun setStudentId(id: String?) {
        putString(KEY_ID, id)
    }

    override fun getBarcode(): String? {
        return getString(KEY_BARCODE)
    }
    override fun setBarcode(barcode: String?) {
        putString(KEY_BARCODE, barcode)
    }

    override fun getLoginToken(): String? {
        return getString(KEY_TOKEN)

    }
    override fun setLoginToken(token: String?) {
        putString(KEY_TOKEN, token)
    }

    override fun activateBarcode(params: ActivateBarcodeParams, callback: DataCallback<ActivateBarcodeResult>) {
        networkService.getActivateBarcodeResult(params).onResult(
            async = callback.async,
            onSuccess = callback.onSuccess,
            onFail = callback.onFail
        )
    }

    /**
     * Get preference string.
     */
    private fun getString(key: String): String? {
        return pref.getString(key, EMPTY)?.let {
            // Decrypt only if it is not null.
            tryOrNull { AESCrypt.decrypt(BuildConfig.AES_KEY, it) }
        }
    }

    /**
     * Set preference string.
     */
    private fun putString(key: String, value: String?) {
        pref.edit(true) {
            // Put it only when it is not null.
            // Encrypting null is useless.
            putString(key, value?.let {
                tryOrNull { AESCrypt.encrypt(BuildConfig.AES_KEY, it) }
            })
        }
    }

    companion object {
        private const val PREFERENCE_STUDENT_INFO = "studentInfo"

        private const val KEY_ID = "studentId"
        private const val KEY_BARCODE = "barcode"
        private const val KEY_TOKEN = "token"

        private const val EMPTY = ""
    }
}
