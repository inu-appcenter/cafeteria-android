package org.inu.cafeteria.feature.cafeteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.common.extension.defaultDataErrorHandle
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.CafeteriaRepository
import org.inu.cafeteria.usecase.GetFoodMenu
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
    val food: LiveData<FoodMenu> = _food

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
            result.onSuccess {list ->
                cafeteria?.let {
                    _food.value = list.find { it.cafeteriaNumber == cafeteria?.key }
                    Timber.i("Successfully loaded all food menu.")
                } ?: Timber.w("Got food menu but failed to set food because cafeteria is not set.")
            }.onError { defaultDataErrorHandle(it) }
        }
    }
}