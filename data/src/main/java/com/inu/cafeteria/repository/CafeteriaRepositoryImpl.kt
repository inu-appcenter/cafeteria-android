/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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