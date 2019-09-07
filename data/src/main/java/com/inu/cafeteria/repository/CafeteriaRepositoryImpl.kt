/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import com.inu.cafeteria.exception.BodyParseException
import com.inu.cafeteria.exception.DataNotFoundException
import com.inu.cafeteria.extension.getFormatedDate
import com.inu.cafeteria.extension.onNull
import com.inu.cafeteria.extension.onResult
import com.inu.cafeteria.model.Cache
import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.parser.CafeteriaParser
import com.inu.cafeteria.parser.FoodMenuParser
import com.inu.cafeteria.service.CafeteriaNetworkService
import timber.log.Timber
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
                onFail = { failure = it }
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

    override fun getCafeteriaByCafeteriaNumber(key: Int, callback: DataCallback<Cafeteria>) {
        getAllCafeteria(
            DataCallback(
                async = callback.async,
                onSuccess = {
                    if (it.getOrNull(key) != null) {
                        callback.onSuccess(it[key])
                    } else {
                        callback.onFail(DataNotFoundException())
                    }
                },
                onFail = callback.onFail
            )
        )
    }

    override fun getFoodMenuByCafeteriaNumber(key: Int, callback: DataCallback<FoodMenu>) {
        getAllFoodMenu(
            DataCallback(
                async = callback.async,
                onSuccess = {
                    if (it.getOrNull(key) != null) {
                        callback.onSuccess(it[key])
                    } else {
                        callback.onFail(DataNotFoundException())
                    }
                },
                onFail = callback.onFail
            )
        )
    }
}