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

package com.inu.cafeteria.feature.discount

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Account
import com.inu.cafeteria.model.NoAccountException
import com.inu.cafeteria.service.AccountService
import com.inu.cafeteria.usecase.ActivateBarcode
import com.inu.cafeteria.usecase.CreateBarcode
import com.inu.cafeteria.usecase.GetSavedAccount
import com.inu.cafeteria.usecase.RememberedLogin
import com.inu.cafeteria.util.SingleLiveEvent
import org.koin.core.inject

class DiscountViewModel : BaseViewModel() {

    private val createBarcode: CreateBarcode by inject()
    private val rememberedLogin: RememberedLogin by inject()
    private val activateBarcode: ActivateBarcode by inject()
    private val getSavedAccount: GetSavedAccount by inject()

    private val accountService: AccountService by inject()

    private val navigator: Navigator by inject()

    private val _barcodeBitmap = MutableLiveData<Bitmap>()
    val barcodeBitmap: LiveData<Bitmap> = _barcodeBitmap

    private val _barcodeCardReady = MutableLiveData<Boolean>(false)
    val barcodeCardReady: LiveData<Boolean> = _barcodeCardReady

    fun load() {
        _barcodeCardReady.value = false

        when {
            accountService.isLoggedIn() -> showBarcode()
            accountService.hasSavedAccount() -> loginAndShowBarcode()
            else -> {
                // Prompt user to login
            }
        }
    }

    fun onClickLogin() {
        navigator.showLogin()
    }

    private fun showBarcode() {
        getSavedAccount(Unit) {
            it.onSuccess(::showBarcodeForAccount).onError(::handleFailure)
        }
    }

    private fun loginAndShowBarcode() {
        rememberedLogin(Unit) {
            it.onSuccess { showBarcode() }.onError(::handleLoginFailure)
        }
    }

    private fun showBarcodeForAccount(account: Account) {
        createBarcode(Triple(account.barcode, 600, 300)) {
            it.onSuccess(::handleBarcodeImage).onError(::handleFailure)
        }

        activateBarcode(Unit) {
            it.onError(::handleFailure)
        }
    }

    private fun handleBarcodeImage(image: Bitmap) {
        _barcodeBitmap.value = image
        _barcodeCardReady.value = true
    }

    private fun handleLoginFailure(exception: Exception) {
        when (exception) {
            is NoAccountException -> {}
            else -> handleFailure(exception)
        }
    }
}