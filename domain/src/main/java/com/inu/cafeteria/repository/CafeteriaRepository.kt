/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

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