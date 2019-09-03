package org.inu.cafeteria.usecase

import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.model.scheme.LogoutParams
import org.inu.cafeteria.model.scheme.LogoutResult
import org.inu.cafeteria.repository.LoginRepository
import java.io.IOException

class Logout(
    private val loginRepo: LoginRepository
) : UseCase<LogoutParams, LogoutResult>() {

    override fun run(params: LogoutParams) = Result.of {
        return@of try {
            loginRepo.logout(params)
                .execute()
                .let { it.takeIf { it.isSuccessful } ?: throw ResponseFailException() }
                .also { loginRepo.setLoginIn(false) }
                .body()
                ?: throw RuntimeException("Body is null.")
        } catch (e: IOException) {
            throw ServerNoResponseException()
        }
    }
}