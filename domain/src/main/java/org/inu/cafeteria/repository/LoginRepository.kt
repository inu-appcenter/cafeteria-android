package org.inu.cafeteria.repository

import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.model.scheme.LogoutParams
import org.inu.cafeteria.model.scheme.LogoutResult
import retrofit2.Call

abstract class LoginRepository : Repository() {

    abstract fun setLoginIn(isLoggedIn: Boolean)
    abstract fun isLoggedIn(): Boolean

    abstract fun login(params: LoginParams): Call<LoginResult>
    abstract fun logout(params: LogoutParams): Call<LogoutResult>
}