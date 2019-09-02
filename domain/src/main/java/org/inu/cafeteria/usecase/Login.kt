package org.inu.cafeteria.usecase

import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.service.CafeteriaNetworkService

class Login(
    private val networkService: CafeteriaNetworkService
) : UseCase<LoginParams, LoginResult>() {

    override fun run(params: LoginParams): Result<LoginResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}