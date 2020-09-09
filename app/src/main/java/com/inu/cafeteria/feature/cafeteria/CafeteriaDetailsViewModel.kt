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
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.extension.onNull
import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.repository.CafeteriaRepository
import com.inu.cafeteria.usecase.GetFoodMenu
import org.koin.core.inject
import timber.log.Timber

class CafeteriaDetailsViewModel : BaseViewModel() {

    private val getFoodMenu: GetFoodMenu by inject()
    private val cafeteriaRepo: CafeteriaRepository by inject()

    var cafeteria: Cafeteria? = null
        private set

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _food = MutableLiveData<FoodMenu>()
    val food: MutableLiveData<FoodMenu> = _food

    init {
        failables += this
    }

    /**
     * This must be called in fragment's onCreate.
     */

fun startWithCafeteria(cafeteria: Cafeteria?) {
        cafeteria?.let {
            this.cafeteria = it
            _title.value = it.name
            Timber.i("Cafeteria is set.")
        } ?: Timber.i("Cafeteria is null.")
    }

    /**
     * Load food menu for this cafeteria.
     * This is done asynchronously.
     *
     * After successful fetch, [food] will be posted value
     * and the fragment will resume transition.
     *
     * @see [CafeteriaDetailFragment].
     *
     * @param clearCache whether or not to clear repository cache.
     */

fun loadFoodMenu(clearCache: Boolean = false) {
        if (cafeteria == null) {
            Timber.i("Cannot load food menu. Cafeteria is null.")
            return
        }

        if (clearCache) {
            cafeteriaRepo.invalidateCache()
        }

        getFoodMenu(Unit) { result ->
            result.onSuccess { list ->
                cafeteria?.let {
                    val found = list.find { it.cafeteriaNumber == cafeteria?.key }
                    _food.value = found

                    found
                        ?.let { Timber.i("Found food menu for the cafeteria and successfully set them.") }
                        .onNull { Timber.i("Got all food menu but the one for this cafeteria.") }

                } ?: Timber.w("Got food menu but failed to set food because cafeteria is not set.")
            }.onError { defaultDataErrorHandle(it) }
        }
    }
}