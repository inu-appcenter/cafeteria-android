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

package com.inu.cafeteria.service

import com.inu.cafeteria.entities.Account
import com.inu.cafeteria.model.NoAccountException
import com.inu.cafeteria.repository.AccountRepository

class AccountService(
    private val accountRepo: AccountRepository
) {

    fun isLoggedIn() = accountRepo.isLoggedIn()

    fun login(id: Int, password: String) {
        val account = accountRepo.firstLogin(id, password)

        accountRepo.saveAccount(account)
    }

    fun rememberedLogin() {
        val account = accountRepo.getSavedAccount() ?: throw NoAccountException("No saved account.")

        val refreshedAccount = accountRepo.rememberedLogin(account.id, account.token)

        accountRepo.saveAccount(refreshedAccount)
    }

    fun hasSavedAccount(): Boolean = getSavedAccount() != null

    fun getSavedAccount(): Account? = accountRepo.getSavedAccount()

    fun activateBarcode() = accountRepo.activateBarcode()
}