/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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