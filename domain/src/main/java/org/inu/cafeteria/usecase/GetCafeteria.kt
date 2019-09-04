package org.inu.cafeteria.usecase

import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.CafeteriaRepository
import org.inu.cafeteria.repository.Repository
import timber.log.Timber

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

        Timber.i("AFTER EXECUTE, ARE THEY NULL? RESULT: ${result == null}, FAILURE: ${failure == null}")

        failure?.let { throw it }

        return@of result!!
    }
}