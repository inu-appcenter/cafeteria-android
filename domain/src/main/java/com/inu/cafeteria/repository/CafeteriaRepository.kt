package com.inu.cafeteria.repository

import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.model.json.Cafeteria

/**
 * This repository use callback model.
 */
abstract class CafeteriaRepository : Repository() {

    abstract fun invalidateCache()

    abstract fun getAllCafeteria(callback: DataCallback<List<Cafeteria>>)
    abstract fun getAllFoodMenu(callback: DataCallback<List<FoodMenu>>)

    abstract fun getCafeteriaByCafeteriaNumber(key: Int, callback: DataCallback<Cafeteria>)
    abstract fun getFoodMenuByCafeteriaNumber(key: Int, callback: DataCallback<FoodMenu>)
}