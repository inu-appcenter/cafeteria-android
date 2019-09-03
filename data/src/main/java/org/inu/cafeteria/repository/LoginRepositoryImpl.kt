package org.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import org.inu.cafeteria.extension.onSuccess
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.model.scheme.LogoutParams
import org.inu.cafeteria.model.scheme.LogoutResult
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.math.log

class LoginRepositoryImpl(
    private val networkService: CafeteriaNetworkService
) : LoginRepository() {

    private var login = false

    override fun setLoginIn(isLoggedIn: Boolean) {
        login = isLoggedIn
        Timber.i("Login state update. Logged ${if (isLoggedIn) "in" else "out"}.")
    }

    override fun isLoggedIn(): Boolean {
        return login
    }

    override fun login(params: LoginParams): Call<LoginResult> {
        return networkService.getLoginResult(params)
    }

    override fun logout(params: LogoutParams): Call<LogoutResult> {
        return networkService.getLogoutResult(params)
    }
}