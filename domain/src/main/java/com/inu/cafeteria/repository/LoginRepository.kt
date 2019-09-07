package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.LoginParams
import com.inu.cafeteria.model.scheme.LoginResult
import com.inu.cafeteria.model.scheme.LogoutParams
import com.inu.cafeteria.model.scheme.LogoutResult

abstract class LoginRepository : Repository() {

    abstract fun isLoggedIn(): Boolean

    abstract fun login(params: LoginParams, callback: DataCallback<LoginResult>)
    abstract fun logout(params: LogoutParams, callback: DataCallback<LogoutResult>)
}