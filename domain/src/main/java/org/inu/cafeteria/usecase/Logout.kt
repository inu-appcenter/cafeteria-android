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
import org.inu.cafeteria.repository.Repository
import timber.log.Timber
import java.io.IOException

class Logout(
    private val loginRepo: LoginRepository
) : UseCase<LogoutParams, LogoutResult>() {

    override fun run(params: LogoutParams) = Result.of {
        var result: LogoutResult? = null
        var failure: Exception? = null

        loginRepo.logout(params, Repository.DataCallback(
            async = false,
            onSuccess = { result = it },
            onFail = { failure = it }
        ))

        failure?.let { throw it }

        return@of result!!
    }
}