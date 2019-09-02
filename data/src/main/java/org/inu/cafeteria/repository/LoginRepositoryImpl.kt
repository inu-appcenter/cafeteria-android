package org.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.model.scheme.LogoutParams
import org.inu.cafeteria.model.scheme.LogoutResult
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call

class LoginRepositoryImpl(
    private val networkService: CafeteriaNetworkService
) : LoginRepository() {

    override fun login(params: LoginParams): Call<LoginResult> {
        return networkService.getLoginResult(params)
    }

    override fun logout(params: LogoutParams): Call<LogoutResult> {
        return networkService.getLogoutResult(params)
    }
}