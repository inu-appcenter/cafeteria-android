package org.inu.cafeteria.usecase

import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.repository.LoginRepository
import org.inu.cafeteria.repository.Repository
import org.inu.cafeteria.service.CafeteriaNetworkService
import java.io.IOException
import kotlin.math.log

class Login(
    private val loginRepo: LoginRepository
) : UseCase<LoginParams, LoginResult>() {

    override fun run(params: LoginParams) = Result.of {
        return@of loginRepo.login(params, Repository.DataCallback(
            onSuccess =
        ))
    }
}