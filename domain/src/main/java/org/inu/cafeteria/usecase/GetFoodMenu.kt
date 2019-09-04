package org.inu.cafeteria.usecase

import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.CafeteriaRepository
import org.inu.cafeteria.repository.Repository

class GetFoodMenu(
    private val cafeteriaRepo: CafeteriaRepository
) : UseCase<Unit, List<FoodMenu>>() {

    override fun run(params: Unit) = Result.of {
        var result: List<FoodMenu>? = null
        var failure: Exception? = null

        cafeteriaRepo.getAllFoodMenu(
            Repository.DataCallback(
                async = false,
                onSuccess = { result = it },
                onFail = { failure = it }
            )
        )

        failure?.let { throw it }

        return@of result!!
    }
}