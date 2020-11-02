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

package com.inu.cafeteria.feature.cafeteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.feature.main.MenuView
import com.inu.cafeteria.usecase.GetCafeteria
import org.koin.core.inject

class CafeteriaDetailViewModel : BaseViewModel() {

    private val getCafeteria: GetCafeteria by inject()

    private val _menus = MutableLiveData<List<MenuView>>()
    val menus: LiveData<List<MenuView>> = _menus

    fun load(cafeteriaId: Int, date: String, onSetToolBarTitle: (String) -> Unit) {
        getCafeteria(date) { result ->
            result
                .onSuccess { allCafeteria ->
                    allCafeteria
                        .find { it.id == cafeteriaId }
                        ?.also(::handleCafeteria)
                        ?.also { onSetToolBarTitle(it.name) }
                        ?: handleFailure(Exception("Cafeteria with id $cafeteriaId not found!"))
                }
                .onError(::handleFailure)
        }
    }

    private fun handleCafeteria(cafeteria: Cafeteria) {
        _menus.postValue(cafeteria.corners.map { corner ->
            corner.menus.map { menu ->
                MenuView.fromCornerAndMenu(corner, menu)
            }
        }.flatten())
    }
}