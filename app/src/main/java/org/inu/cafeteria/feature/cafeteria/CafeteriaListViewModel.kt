package org.inu.cafeteria.feature.cafeteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.common.extension.defaultDataErrorHandle
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.CafeteriaRepository
import org.inu.cafeteria.usecase.GetCafeteria
import org.koin.core.inject

class CafeteriaListViewModel : BaseViewModel() {

    private val getCafeteria: GetCafeteria by inject()

    private val cafeteriaRepo: CafeteriaRepository by inject()

    private val _cafeteria = MutableLiveData<List<Cafeteria>>()
    val cafeteria: LiveData<List<Cafeteria>> = _cafeteria

    init {
        failables += this
    }

    fun loadAll(clearCache: Boolean = false) {
        if (clearCache) {
            cafeteriaRepo.invalidateCache()
        }

        getCafeteria(Unit) { result ->
            result.onSuccess { _cafeteria.value = it }.onError { defaultDataErrorHandle(it) }
        }
    }
}