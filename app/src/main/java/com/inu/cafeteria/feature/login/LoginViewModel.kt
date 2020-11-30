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

package com.inu.cafeteria.feature.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.onChanged
import com.inu.cafeteria.service.AccountService
import com.inu.cafeteria.usecase.Login
import org.koin.core.inject

class LoginViewModel : BaseViewModel() {

    private val login: Login by inject()
    private val accountService: AccountService by inject()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _formValid = MutableLiveData(false)
    val formValid: LiveData<Boolean> = _formValid

    val loggedInStatus = accountService.loggedInStatus()

    val userInputId = ObservableField<String>().apply {
        onChanged {
            _formValid.value = isItOkayToSendLoginRequestNow()
        }
    }

    val userInputPassword = ObservableField<String>().apply {
        onChanged {
            _formValid.value = isItOkayToSendLoginRequestNow()
        }
    }

    fun performLogin() {
        if (!isItOkayToSendLoginRequestNow()) {
            handleFailure(R.string.error_check_login_form)
            return
        }

        val id = userInputId.get()!!.toInt()
        val password = userInputPassword.get()!!

        _loading.value = true

        login(Pair(id, password)) {
            it
                .onError(::handleFailure)
                .finally { _loading.value = false }
        }
    }

    private fun isItOkayToSendLoginRequestNow(): Boolean {
        val id = userInputId.get() ?: return false
        if (id.isBlank()) {
            return false
        }
        if (id.toIntOrNull() == null) {
            return false
        }

        val password = userInputPassword.get() ?: return false
        if (password.isBlank()) {
            return false
        }

        return true
    }

    override fun handleFailure(e: Exception) {
        handleFailure(R.string.error_login_fail)
    }
}