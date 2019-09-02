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
    context: Context,
    private val networkService: CafeteriaNetworkService
) : LoginRepository() {

    private val studentPref = context.getSharedPreferences(
        PREFERENCE_STUDENT_INFO,
        Activity.MODE_PRIVATE
    )

    override fun login(params: LoginParams): Call<LoginResult> {
        return networkService.getLoginResult(params)
    }

    override fun logout(params: LogoutParams): Call<LogoutResult> {
        return networkService.getLogoutResult(params)
    }

    override fun getToken(): String? {
        studentPref.getString()
    }

    override fun setToken(token: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val PREFERENCE_STUDENT_INFO = "studentInfo"
        private const val KEY_TOKEN = "token"
    }
}