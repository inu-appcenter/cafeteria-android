package org.inu.cafeteria.repository

import org.inu.cafeteria.exception.BodyParseException
import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.extension.onNull
import org.inu.cafeteria.extension.onResult
import org.inu.cafeteria.extension.onSuccess
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.parser.CafeteriaParser
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call

class CafeteriaRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val paresr: CafeteriaParser
) : CafeteriaRepository() {

    override fun getAllCafeteria(callback: DataCallback<List<Cafeteria>>) {
        networkService.getCafeteria().onResult(
            onSuccess = {
                paresr.parse(it)
                    .onNull { callback.onFail(BodyParseException()) }
                    ?.let { callback.onSuccess(it) }
            },
            onFail = callback.onFail
        )
    }
}