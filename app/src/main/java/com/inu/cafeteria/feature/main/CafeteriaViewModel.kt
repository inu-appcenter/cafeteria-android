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

package com.inu.cafeteria.feature.main

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.extension.afterDays
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.usecase.GetCafeteria
import org.koin.core.inject
import java.util.*

class CafeteriaViewModel : BaseViewModel() {

    private val getCafeteria: GetCafeteria by inject()

    private val _cafeteria = MutableLiveData<List<CafeteriaView>>()
    val cafeteria: LiveData<List<CafeteriaView>> = _cafeteria

    private val _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> = _loading

    private val cafeteriaCache: MutableMap<String, List<Cafeteria>> = mutableMapOf()

    fun preFetch(howMany: Int) {
        (0 until howMany).map(::daysAfter).forEach { date ->
            getCafeteria(date) { result ->
                result
                    .onSuccess { saveToCache(date, it) }
                    .onError(::handleFailure)
            }
        }
    }

    fun onSelectDateTab(tabPosition: Int) {
        fetch(daysAfter(tabPosition))
    }

    private fun daysAfter(days: Int): String = Date().afterDays(days).format()

    private fun fetch(date: String = daysAfter(0)) {
        getFromCache(date)?.let {
            handleCafeteria(it)
            return
        }

        startLoading()

        getCafeteria(date) { result ->
            result
                .onSuccess { saveToCache(date, it) }
                .onSuccess(::handleCafeteria)
                .onError(::handleFailure)

            finishLoading()
        }
    }

    private fun getFromCache(date: String) = cafeteriaCache[date]

    private fun saveToCache(date: String, data: List<Cafeteria>) {
        cafeteriaCache[date] = data
    }

    private fun startLoading() {
        _loading.value = true
    }

    private fun finishLoading() {
        // God damn point: Even if the network job is finished and the result arrived,
        // we have to wait for a few more moments before we show up the cafeteria_recycler.
        // Otherwise it will slow down UI rendering.
        // We needed to right like below:
        //
        // Handler(Looper.getMainLooper()).postDelayed({
        //     _loading.value = false
        // }, 250)
        //
        // However it doesn't matter because we can pre-fetch all data(number of them are fixed!).

        _loading.value = false
    }

    private fun handleCafeteria(allCafeteria: List<Cafeteria>) {
        val result = allCafeteria.map { cafeteria ->
            CafeteriaView(
                id = cafeteria.id,
                name = cafeteria.displayName ?: cafeteria.name,
                wholeMenus = cafeteria.corners.map { corner ->
                    corner.menus.map { menu ->
                        MenuView.fromCornerAndMenu(corner, menu)
                    }
                }.flatten()
            )
        }

        this._cafeteria.value = result
    }

    private fun handleFailure(e: Exception) {
        Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
    }

    fun onViewMore(cafeteriaView: CafeteriaView) {
        Toast.makeText(mContext, "Not implemented yet :)", Toast.LENGTH_SHORT).show()
    }
}