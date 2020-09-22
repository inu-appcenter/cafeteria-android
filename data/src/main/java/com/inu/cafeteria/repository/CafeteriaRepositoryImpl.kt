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

import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.entities.Corner
import com.inu.cafeteria.entities.Menu
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.extension.getOrNull
import com.inu.cafeteria.model.scheme.CafeteriaResult
import com.inu.cafeteria.model.scheme.CornerResult
import com.inu.cafeteria.model.scheme.MenuResult
import com.inu.cafeteria.service.CafeteriaNetworkService
import com.inu.cafeteria.util.Cache
import timber.log.Timber
import java.util.*

class CafeteriaRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
) : CafeteriaRepository() {

    // These have app-wide lifecycle. Fetch runs only for once.
    private val cafeteriaCache = Cache<List<CafeteriaResult>>().apply { set(null) }
    private val cornerCache = Cache<List<CornerResult>>().apply { set(null) }

    override fun getAllCafeteria(date: String?): List<Cafeteria> {
        val cafeteria = cachedFetch(cafeteriaCache) {
            networkService.getCafeteria().getOrNull()
        } ?: return listOf()

        val corners = cachedFetch(cornerCache) {
            networkService.getCorners().getOrNull()
        } ?: return listOf()

        val menus = networkService.getMenus(date).getOrNull()
            ?: return listOf()

        return ResultGatherer(cafeteria, corners, menus).combine()
    }

    @Synchronized
    private fun <T> cachedFetch(cache: Cache<T>, fetch: () -> T?): T? {
        return cache.get() ?: fetch()?.also(cache::set)
    }

    class ResultGatherer(
        private val cafeteriaResult: List<CafeteriaResult>,
        private val cornerResults: List<CornerResult>,
        private val menuResults: List<MenuResult>
    ) {

        fun combine(): List<Cafeteria> {
            return cafeteriaResult.map {
                Cafeteria(
                    id = it.id,
                    name = it.name,
                    displayName = it.displayName,
                    supportMenu = it.supportMenu,
                    supportDiscount = it.supportDiscount,
                    supportNotification = it.supportNotification,
                    corners = cornersOfCafeteria(it.id)
                )
            }
        }

        private fun cornersOfCafeteria(cafeteriaId: Int): List<Corner> {
            return cornerResults
                .filter { it.cafeteriaId == cafeteriaId }
                .map {
                    Corner(
                        id = it.id,
                        name = it.name,
                        displayName = it.displayName,
                        availableAt = it.availableAt,
                        menus = menusOfCorner(it.id)
                    )
                }
        }

        private fun menusOfCorner(cornerId: Int): List<Menu> {
            return menuResults
                .filter { it.cornerId == cornerId }
                .map {
                    Menu(
                        foods = it.foods.split(' '),
                        price = it.price,
                        calorie = it.calorie
                    )
                }
        }
    }
}