package org.inu.cafeteria.feature.login

import android.app.Activity
import org.inu.cafeteria.R
import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.common.extension.finishActivity
import org.inu.cafeteria.common.extension.handleRetrofitException
import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.repository.StudentInfoRepository
import org.inu.cafeteria.usecase.Login
import org.koin.android.ext.android.inject
import org.koin.core.inject
import timber.log.Timber

class LoginViewModel : BaseViewModel() {

    private val login: Login by inject()
    private val studentInfoRepo: StudentInfoRepository by inject()

    private val navigator: Navigator by inject()

    init {
        failables += this
        failables += login
        failables += studentInfoRepo
        failables += navigator
    }

    /**
     * Try auto login.
     *
     * @param onPass launched when no token saved.
     * @param onSuccess launched when successfully login with token.
     * @param onFail launched when request failed.
     */
    fun tryAutoLogin(
        onPass: () -> Unit,
        onSuccess: (LoginResult) -> Unit,
        onFail: (Exception) -> Unit
    ) {
        val token = studentInfoRepo.getLoginToken()

        if (token.isNullOrEmpty()) {
            onPass()
            return
        }

        login(LoginParams.ofUsingToken(token)) {
            it.onSuccess(onSuccess).onError(onFail)
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
        login(LoginParams.ofFirstLogin(id = id, pw = pw, save = auto)) {
            it.onSuccess(onSuccess).onError(onFail)
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
            else -> handleRetrofitException(e)
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
            else -> handleRetrofitException(e)
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
     * Navigate to CafeteriaActivity and close current activity.
     */
    fun showMain(fragment: BaseFragment) {
        navigator.showMain()
        fragment.finishActivity()
    }
}