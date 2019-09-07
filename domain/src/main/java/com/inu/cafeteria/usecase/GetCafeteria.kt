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
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.repository.CafeteriaRepository
import com.inu.cafeteria.repository.Repository

class GetCafeteria(
    private val cafeteriaRepo: CafeteriaRepository
) : UseCase<Unit, List<Cafeteria>>() {

    override fun run(params: Unit) = Result.of {
        var result: List<Cafeteria>? = null
        var failure: Exception? = null

        cafeteriaRepo.getAllCafeteria(Repository.DataCallback(
            async = false,
            onSuccess = { result = it },
            onFail = { failure = it }
        ))

        failure?.let { throw it }

        return@of result!!
    }
}