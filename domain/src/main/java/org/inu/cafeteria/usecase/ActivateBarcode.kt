package org.inu.cafeteria.usecase

import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.scheme.ActivateBarcodeParams
import org.inu.cafeteria.model.scheme.ActivateBarcodeResult
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.repository.Repository
import org.inu.cafeteria.repository.StudentInfoRepository
import java.io.IOException

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