package org.inu.cafeteria.repository

import org.inu.cafeteria.exception.BodyParseException
import org.inu.cafeteria.extension.onNull
import org.inu.cafeteria.extension.onResult
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.parser.CafeteriaParser
import org.inu.cafeteria.service.CafeteriaNetworkService
import timber.log.Timber

class CafeteriaRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val parser: CafeteriaParser
) : CafeteriaRepository() {

    override fun getAllCafeteria(callback: DataCallback<List<Cafeteria>>) {
        networkService.getCafeteria().onResult(
            async = callback.async,
            onSuccess = { json ->
                parser.parse(json)
                    ?.let { callback.onSuccess(it) }
                    .onNull { callback.onFail(BodyParseException()) }
            },
            onFail = callback.onFail
        )
    }
}