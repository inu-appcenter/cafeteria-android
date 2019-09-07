/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

/**
 * You have to create your own implementation of this repository.
 * It is forbidden to upload the server url to VCS.
 */
abstract class PrivateRepository : Repository() {

    abstract fun getServerBaseUrl(): String
}