/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import android.util.Base64
import com.inu.cafeteria.data.BuildConfig

class PrivateRepositoryImpl : PrivateRepository() {
    override fun getServerBaseUrl(): String {
        return String(Base64.decode(BuildConfig.BASE_URL, 0))
    }
}
