package org.inu.cafeteria.usecase

import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.parser.CafeteriaParser
import org.inu.cafeteria.repository.CafeteriaRepository
import java.io.IOException

class GetCafeteria(
    private val cafeteriaRepo: CafeteriaRepository,
    private val parser: CafeteriaParser
) : UseCase<Unit, List<Cafeteria>>() {

    override fun run(params: Unit) = Result.of {
        return@of try {
            cafeteriaRepo.getAllCafeteria()
                .execute()
                .let { it.takeIf { it.isSuccessful } ?: throw ResponseFailException() }
                .body()
                ?.asJsonArray
                ?.let { parser.parse(it) }
                ?: throw RuntimeException("Body is null.")
        } catch (e: IOException) {
            throw ServerNoResponseException()
        }
    }
}