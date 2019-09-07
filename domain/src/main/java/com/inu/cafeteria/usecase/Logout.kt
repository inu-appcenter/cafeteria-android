/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.usecase

import com.inu.cafeteria.functional.Result
import com.inu.cafeteria.interactor.UseCase
import com.inu.cafeteria.model.scheme.LogoutParams
import com.inu.cafeteria.model.scheme.LogoutResult
import com.inu.cafeteria.repository.LoginRepository
import com.inu.cafeteria.repository.Repository

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