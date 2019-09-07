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
import com.inu.cafeteria.model.scheme.ActivateBarcodeParams
import com.inu.cafeteria.model.scheme.ActivateBarcodeResult
import com.inu.cafeteria.repository.Repository
import com.inu.cafeteria.repository.StudentInfoRepository

class ActivateBarcode(
    private val studentInfoRepo: StudentInfoRepository
) : UseCase<ActivateBarcodeParams, ActivateBarcodeResult>() {

    override fun run(params: ActivateBarcodeParams) = Result.of {
        var result: ActivateBarcodeResult? = null
        var failure: Exception? = null

        studentInfoRepo.activateBarcode(params, Repository.DataCallback(
            async = false,
            onSuccess = { result = it },
            onFail = { failure = it }
        ))

        failure?.let { throw it }

        return@of result!!
    }
}