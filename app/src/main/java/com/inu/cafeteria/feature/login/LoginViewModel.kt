/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.common.extension.finishActivity
import com.inu.cafeteria.exception.ResponseFailException
import com.inu.cafeteria.model.scheme.LoginParams
import com.inu.cafeteria.model.scheme.LoginResult
import com.inu.cafeteria.repository.StudentInfoRepository
import com.inu.cafeteria.usecase.Login
import org.koin.core.inject
import timber.log.Timber

class LoginViewModel : BaseViewModel() {

    private val login: Login by inject()
    private val studentInfoRepo: StudentInfoRepository by inject()

    private val navigator: Navigator by inject()

    /**
     * Represents login status.
     * Fragment will observe this and take an adequate action.
     */
    private val _loginInProgress = MutableLiveData<Boolean>()
    var loginInProgress: LiveData<Boolean> = _loginInProgress

    init {
        failables += this
        failables += login
        failables += studentInfoRepo
        failables += navigator
    }

    /**
     * Try auto login.
     *
     * @param onNoToken launched when no token saved.
     * @param onSuccess launched when successfully login with token.
     * @param onFail launched when request failed.
     */
    fun tryAutoLogin(
        onNoToken: () -> Unit,
        onSuccess: (LoginResult) -> Unit,
        onFail: (Exception) -> Unit
    ) {
        _loginInProgress.value = true

        val token = studentInfoRepo.getLoginToken()

        if (token.isNullOrEmpty()) {
            onNoToken()
            _loginInProgress.value = false
            return
        }

        login(LoginParams.ofUsingToken(token)) {
            it.onSuccess(onSuccess).onError(onFail)

            // Finally
            _loginInProgress.value = false
        }
    }

    /**
     * Try login with given ID and password.
     *
     * @param id student id.
     * @param pw the password.  TODO: ENCRYPT IT.
     * @param auto to save login data or not.
     * @param onSuccess launched when successfully logged in.
     * @param onFail launched when request failed.
     */
    fun tryLoginWithIdAndPw(
        id: String,
        pw: String,
        auto: Boolean,
        onSuccess: (LoginResult) -> Unit,
        onFail: (Exception) -> Unit
    ) {
        _loginInProgress.value = true

        login(LoginParams.ofFirstLogin(id = id, pw = pw, save = auto)) {
            it.onSuccess(onSuccess).onError(onFail)

            // Finally
            _loginInProgress.value = false
        }
    }

    /**
     * We need to invalidate stored student id if we failed to auto login.
     */
    fun handleAutoLoginFailure(e: Exception) {
        when (e) {
            is ResponseFailException -> {
                studentInfoRepo.invalidate()
                fail(R.string.fail_token_invalid, show = true)
                Timber.w("Token is invalid. Invalidate all student data.")
            }
            else -> defaultDataErrorHandle(e)
        }
    }

    /**
     * Handle login failures.
     */
    fun handleLoginFailure(e: Exception) {
        when (e) {
            is ResponseFailException -> {
                fail(R.string.fail_wrong_auth, show = true)
            }
            else -> defaultDataErrorHandle(e)
        }
    }

    /**
     * Save token and barcode obtained from successful login.
     */
    fun saveLoginResult(result: LoginResult, id: String) {
        with(studentInfoRepo) {
            setStudentId(id)
            setLoginToken(result.token)
            setBarcode(result.barcode)
        }
    }

    /**
     * Navigate to MainActivity and close current activity.
     */
    fun showMain(fragment: BaseFragment) {
        navigator.showMain()
        fragment.finishActivity()
    }
}