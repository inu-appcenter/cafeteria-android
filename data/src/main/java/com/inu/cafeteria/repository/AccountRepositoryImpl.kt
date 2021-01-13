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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.entities.Account
import com.inu.cafeteria.exception.NullBodyException
import com.inu.cafeteria.extension.getOrThrow
import com.inu.cafeteria.retrofit.CafeteriaNetworkService
import com.inu.cafeteria.retrofit.scheme.LoginParams

class AccountRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val db: SharedPreferenceWrapper
) : AccountRepository {

    private var loggedIn = MutableLiveData(false)

    override fun isLoggedIn(): Boolean {
        return loggedIn.value!!
    }

    override fun isLoggedInLiveData(): LiveData<Boolean> {
        return loggedIn
    }

    override fun firstLogin(id: Int, password: String) =
        login(LoginParams.ofFirstLogin(id, password))

    override fun rememberedLogin(id: Int, token: String) =
        login(LoginParams.ofRememberedLogin(id, token))

    private fun login(param: LoginParams): Account {
        val result = networkService
            .getLoginResult(param)
            .getOrThrow() ?: throw NullBodyException("Login result must not be null!")

        return result.toAccount().apply {
            loggedIn.postValue(true)
        }
    }

    override fun getSavedAccount(): Account? {
        return Account(
            id = db.getInt(KEY_ID).takeIf { it != -1 } ?: return null,
            barcode = db.getString(KEY_BARCODE) ?: return null,
            token = db.getString(KEY_TOKEN) ?: return null
        )
    }

    override fun saveAccount(account: Account) {
        with(db) {
            putInt(KEY_ID, account.id)
            putString(KEY_BARCODE, account.barcode)
            putString(KEY_TOKEN, account.token)
        }
    }

    override fun deleteSavedAccount() {
        with(db) {
            putInt(KEY_ID, -1)
            putString(KEY_BARCODE, null)
            putString(KEY_TOKEN, null)
        }
    }

    override fun activateBarcode() {
        networkService
            .getActivateBarcodeResult()
            .getOrThrow()
    }

    companion object {

        private const val KEY_ID = "com.inu.cafeteria.account_id"
        private const val KEY_BARCODE = "com.inu.cafeteria.account_barcode"
        private const val KEY_TOKEN = "com.inu.cafeteria.account_token"
    }
}