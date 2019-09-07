package com.inu.cafeteria.usecase

import com.inu.cafeteria.functional.Result
import com.inu.cafeteria.interactor.UseCase
import com.inu.cafeteria.model.scheme.LoginParams
import com.inu.cafeteria.model.scheme.LoginResult
import com.inu.cafeteria.repository.LoginRepository
import com.inu.cafeteria.repository.Repository

class Login(
    private val loginRepo: LoginRepository
) : UseCase<LoginParams, LoginResult>() {

    override fun run(params: LoginParams) = Result.of {
        var result: LoginResult? = null
        var failure: Exception? = null

        loginRepo.login(params, Repository.DataCallback(
            async = false,
            onSuccess = { result = it },
            onFail = { failure = it }
        ))

        failure?.let { throw it }

        return@of result!!
    }
}