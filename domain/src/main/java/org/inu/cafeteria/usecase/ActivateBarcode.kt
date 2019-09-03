package org.inu.cafeteria.usecase

import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.scheme.ActivateBarcodeParams
import org.inu.cafeteria.model.scheme.ActivateBarcodeResult
import org.inu.cafeteria.repository.StudentInfoRepository
import java.io.IOException

class ActivateBarcode(
    private val studentInfoRepo: StudentInfoRepository
) : UseCase<ActivateBarcodeParams, ActivateBarcodeResult>() {

    override fun run(params: ActivateBarcodeParams) = Result.of {
        return@of try {
            studentInfoRepo.activateBarcode(params)
                .execute()
                .let { it.takeIf { it.isSuccessful } ?: throw ResponseFailException() }
                .body()
                ?: throw RuntimeException("Body is null.")
        } catch (e: IOException) {
            throw ServerNoResponseException()
        }
    }
}