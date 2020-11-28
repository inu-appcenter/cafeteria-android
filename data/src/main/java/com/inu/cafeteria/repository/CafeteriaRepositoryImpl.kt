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

import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.extension.getOrThrow
import com.inu.cafeteria.retrofit.scheme.CafeteriaResult
import com.inu.cafeteria.retrofit.scheme.CornerResult
import com.inu.cafeteria.retrofit.scheme.MenuResult
import com.inu.cafeteria.retrofit.CafeteriaNetworkService
import com.inu.cafeteria.util.Cache
import com.inu.cafeteria.util.PairedCache
import java.util.*

class CafeteriaRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val db: SharedPreferenceWrapper
) : CafeteriaRepository {

    private val cafeteriaCache = Cache<List<CafeteriaResult>>()
    private val cornerCache = Cache<List<CornerResult>>()
    private val menuCaches = PairedCache<String, List<MenuResult>>()

    override fun getAllCafeteria(date: String?): List<Cafeteria> {
        val cafeteria = cachedFetch(cafeteriaCache) {
            networkService.getCafeteria().getOrThrow()
        } ?: return listOf()

        val corners = cachedFetch(cornerCache) {
            networkService.getCorners().getOrThrow()
        } ?: return listOf()

        val menus = cachedFetch(menuCaches, date ?: Date().format()) {
            networkService.getMenus(date).getOrThrow()
        } ?: return listOf()

        return ResultGatherer(cafeteria, corners, menus).combine()
    }

    override fun getCafeteriaOnly(): List<Cafeteria> {
        val cafeteria = cachedFetch(cafeteriaCache) {
            networkService.getCafeteria().getOrThrow()
        } ?: return listOf()

        return cafeteria.map {
            Cafeteria(
                id = it.id,
                name = it.name,
                displayName = it.displayName,
                supportMenu = it.supportMenu,
                supportDiscount = it.supportDiscount,
                supportNotification = it.supportNotification,
                corners = listOf()
            )
        }
    }

    @Synchronized
    private fun <T> cachedFetch(cache: Cache<T>, fetch: () -> T?): T? {
        return (if (cache.isValid) cache.get() else null) ?: fetch()?.also(cache::set)
    }

    @Synchronized
    private fun <K, V> cachedFetch(cache: PairedCache<K, V>, key: K, fetch: () -> V?): V? {
        return (if (cache.isValid(key)) cache.get(key) else null) ?: fetch()?.also { cache.set(key, it) }
    }


    private val order = MutableLiveData<Array<Int>>()

    override fun getOrder(): Array<Int> {
        return getOrderInternal()
    }

    private fun getOrderInternal(): Array<Int> {
        val result = db.getArrayInt(KEY_ORDER)

        if (result == null) {
            // The first attempt to get order.
            resetOrder()
            return getOrderInternal()
        }

        return result
    }

    override fun setOrder(orderedIds: Array<Int>) {
        order.postValue(orderedIds)

        db.putArrayInt(KEY_ORDER, orderedIds)
    }

    override fun resetOrder() {
        setOrder(getCafeteriaOnly().map { it.id }.toTypedArray())
    }

    companion object {
        private const val KEY_ORDER = "cafeteria_sort_order"
    }
}