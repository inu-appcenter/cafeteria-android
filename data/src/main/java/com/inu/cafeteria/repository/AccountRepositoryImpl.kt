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

import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.entities.Account
import com.inu.cafeteria.extension.getOrNull
import com.inu.cafeteria.model.scheme.LoginParams
import com.inu.cafeteria.service.CafeteriaNetworkService

class AccountRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val db: SharedPreferenceWrapper
) : AccountRepository {

    private var loggedIn = false

    override fun isLoggedIn(): Boolean {
        return loggedIn
    }

    override fun firstLogin(id: Int, password: String) =
        networkService
            .getLoginResult(LoginParams.ofFirstLogin(id, password))
            .getOrNull()
            ?.toAccount()
            ?: throw Exception("Login failed.")

    override fun rememberedLogin(id: Int, token: String) =
        networkService
            .getLoginResult(LoginParams.ofRememberedLogin(id, token))
            .getOrNull()
            ?.toAccount()
            ?: throw Exception("Login failed.")

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

    override fun activateBarcode() {
        
    }

    companion object {

        private const val KEY_ID = "account_id"
        private const val KEY_BARCODE = "account_barcode"
        private const val KEY_TOKEN = "account_token"
    }
}