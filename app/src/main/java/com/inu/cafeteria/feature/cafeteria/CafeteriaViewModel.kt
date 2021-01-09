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

import android.os.Handler
import android.os.Looper
import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.common.onboarding.OnboardingHintEventEmitter
import com.inu.cafeteria.entities.Cafeteria
import com.inu.cafeteria.entities.OnboardingHint
import com.inu.cafeteria.extension.afterDays
import com.inu.cafeteria.extension.applyOrder
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.usecase.GetMenuSupportingCafeteria
import com.inu.cafeteria.usecase.GetSortingOrders
import com.inu.cafeteria.util.SingleLiveEvent
import org.koin.core.inject
import timber.log.Timber
import java.util.*

/**
 * Shared through cafeteria navigation graph.
 */
class CafeteriaViewModel : BaseViewModel() {

    private val getCafeteria: GetMenuSupportingCafeteria by inject()
    private val getSortingOrders: GetSortingOrders by inject()

    private val cafeteriaCache: MutableMap<String, List<Cafeteria>> = mutableMapOf()

    private val navigator: Navigator by inject()

    private val _cafeteria = MutableLiveData<List<CafeteriaView>>()
    val cafeteria: LiveData<List<CafeteriaView>> = _cafeteria

    private val _selected = MutableLiveData<CafeteriaView>()
    val selected: LiveData<CafeteriaView> = _selected

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    var currentDateTabPosition: Int = 0
        private set

    var menuPagePositions = SparseIntArray()
        private set

    val moreClickEvent = SingleLiveEvent<Unit>()
    val animateEvent = SingleLiveEvent<Int>()

    private val sortingHintEmitter = OnboardingHintEventEmitter(OnboardingHint.SortingCafeteria)
    val sortingHintEvent = sortingHintEmitter.event

    fun emitHintEvent() {
        sortingHintEmitter.emitIfAvailable()
    }

    fun markHintShown() {
        sortingHintEmitter.markHintAccepted()
    }

    fun load() {
        if (saySorryIfOffline()) {
            Timber.d("Device is offline. Cancel loading cafeteria view model.")
            return
        }

        Timber.d("Loading cafeteria view model!")

        preFetch(5)
        reselectCurrentDateTab()
    }

    private fun preFetch(howMany: Int) {
        (0 until howMany).map(::daysAfter).forEach { date ->
            getCafeteria(date) { result ->
                result
                    .onSuccess { saveToCache(date, it) }
                    .onError(::handleFailure)
            }
        }
    }

    fun onSelectDateTab(tabPosition: Int) {
        dispatchAnimateEvent(tabPosition)

        currentDateTabPosition = tabPosition

        fetch(daysAfter(tabPosition))
    }

    fun reselectCurrentDateTab() {
        onSelectDateTab(currentDateTabPosition)
    }

    fun onSaveMenuPagePositions(positions: SparseIntArray) {
        menuPagePositions = positions
    }

    private fun dispatchAnimateEvent(tabPosition: Int) {
        // -1: left, 1: right.
        val animationDirection = when {
            tabPosition > currentDateTabPosition -> -1
            tabPosition < currentDateTabPosition -> +1
            else -> 0
        }

        animateEvent.postValue(animationDirection)
    }

    private fun daysAfter(days: Int): String = Date().afterDays(days).format()

    private fun fetch(date: String = daysAfter(0)) {
        startLoading()

        getFromCache(date)?.let {
            handleCafeteria(it)
            finishLoading(slowly = false)
            return
        }

        getCafeteria(date) { result ->
            result
                .onSuccess { saveToCache(date, it) }
                .onSuccess(::handleCafeteria)
                .onError(::handleFailure)
        }
    }

    private fun getFromCache(date: String) = cafeteriaCache[date]

    private fun saveToCache(date: String, data: List<Cafeteria>) {
        cafeteriaCache[date] = data
    }

    private fun startLoading() {
        _loading.value = true
    }

    private fun finishLoading(slowly: Boolean) {
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
        Handler(Looper.getMainLooper()).postDelayed({
            _loading.value = false
        }, if (slowly) 250 else 0)
    }

    fun onClickOptionMenu(menuItemId: Int): Boolean {
        when(menuItemId) {
            R.id.menu_reorder -> showSorting()
        }

        return true
    }

    private fun showSorting() {
        navigator.showSorting()

        sortingHintEmitter.markHintAccepted()
    }

    private fun handleCafeteria(allCafeteria: List<Cafeteria>) {
        getSortingOrders(Unit) {
            it
                .onSuccess{ orderedIds -> handleCafeteriaAndOrder(allCafeteria, orderedIds) }
                .onError(::handleFailure)
        }
    }

    private fun handleCafeteriaAndOrder(allCafeteria: List<Cafeteria>, orderedIds: Array<Int>) {
        handleCafeteriaOrdered(
            allCafeteria.applyOrder(orderedIds) {id}
        )
    }

    private fun handleCafeteriaOrdered(allCafeteriaOrdered: List<Cafeteria>) {
        val result = allCafeteriaOrdered.map { cafeteria ->
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

        _cafeteria.value = result

        finishLoading(slowly = result.isNotEmpty())
    }

    override fun handleFailure(e: Exception) {
        super.handleFailure(e)

        finishLoading(slowly = false)
    }

    fun onViewMore(cafeteriaView: CafeteriaView) {
        _selected.value = cafeteriaView

        moreClickEvent.call()
    }
}