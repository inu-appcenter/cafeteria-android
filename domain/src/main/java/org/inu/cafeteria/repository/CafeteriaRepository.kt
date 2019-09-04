package org.inu.cafeteria.repository

import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria

/**
 * This repository use callback model.
 */
abstract class CafeteriaRepository : Repository() {

    abstract fun getAllCafeteria(callback: DataCallback<List<Cafeteria>>)
    abstract fun getAllFoodMenu(callback: DataCallback<List<FoodMenu>>)
}