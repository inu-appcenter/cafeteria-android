package org.inu.cafeteria.repository

import android.text.format.DateFormat
import org.inu.cafeteria.exception.BodyParseException
import org.inu.cafeteria.extension.getFormatedDate
import org.inu.cafeteria.extension.onNull
import org.inu.cafeteria.extension.onResult
import org.inu.cafeteria.model.Cache
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.parser.CafeteriaParser
import org.inu.cafeteria.parser.FoodMenuParser
import org.inu.cafeteria.service.CafeteriaNetworkService
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CafeteriaRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val cafeteriaParser: CafeteriaParser,
    private val foodMenuParser: FoodMenuParser
) : CafeteriaRepository() {

    private val cafeteriaCache = Cache<List<Cafeteria>>()
    private val foodCache = Cache<List<FoodMenu>>()

    override fun invalidateCache() {
        cafeteriaCache.invalidate()
        foodCache.invalidate()

        Timber.i("All cache invalidated.")
    }

    override fun getAllCafeteria(callback: DataCallback<List<Cafeteria>>) {
        if (cafeteriaCache.isValid) {
            cafeteriaCache.get()?.let {
                callback.onSuccess(it)
                Timber.i("Got all cafeteria from cache!")
                return
            }
        }

        networkService.getCafeteria().onResult(
            async = callback.async,
            onSuccess = { json ->
                cafeteriaParser.parse(json)?.let {
                    callback.onSuccess(it)
                    cafeteriaCache.set(it)
                    Timber.i("Successfully fetched all cafeteria from server.")
                }.onNull { callback.onFail(BodyParseException()) }
            },
            onFail = callback.onFail
        )
    }

    override fun getAllFoodMenu(callback: DataCallback<List<FoodMenu>>) {
        if (foodCache.isValid) {
            foodCache.get()?.let {
                callback.onSuccess(it)
                Timber.i("Got all food menus from cache!")
                return
            }
        }

        val menuSupportCafeteria = mutableListOf<Int>()

        // To parse the food menu, we need the list of
        // cafeteria supporting food menu.
        // Getting all cafeteria will be launched synchronously
        // whatever the callback.async is, or this execution will pass
        // the failure check below.
        var failure: Exception? = null

        getAllCafeteria(
            DataCallback(
                async = false,
                onSuccess = { result ->
                    menuSupportCafeteria.addAll(
                        result
                            .filter { cafeteria -> cafeteria.supportFoodMenu >= 0 }
                            .map { cafeteria -> cafeteria.key }
                    )
                },
                onFail = { failure = it}
            )
        )

        failure?.let {
            Timber.w("")
            callback.onFail(it)
        }

        networkService.getFoods(Calendar.getInstance().getFormatedDate()).onResult(
            async = callback.async,
            onSuccess = { json ->
                foodMenuParser.parse(json, menuSupportCafeteria)?.let {
                    callback.onSuccess(it)
                    foodCache.set(it)
                    Timber.i("Successfully fetched all food menus from server.")
                }.onNull { callback.onFail(BodyParseException()) }
            },
            onFail = callback.onFail
        )
    }
}