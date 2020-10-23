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

package com.inu.cafeteria.injection

import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.repository.*
import com.inu.cafeteria.service.AccountService
import com.inu.cafeteria.usecase.*
import org.koin.dsl.module

val myModules = module {

    /*****************************
     * General
     *****************************/

    /** Navigator */

    single {
        Navigator(
            context = get()
        )
    }

    /** Network Service */

    single {
        RetrofitFactory.createCafeteriaNetworkService(
            context = get()
        )
    }

    single {
        EventHub()
    }

    /** DB */
    single {
        SharedPreferenceWrapper(get())
    }


    /*****************************
     * Service
     *****************************/

    single {
        AccountService(
            accountRepo = get()
        )
    }


    /*****************************
     * Repository
     *****************************/

    /** Cafeteria Repository */

    single {
        CafeteriaRepositoryImpl(
            networkService = get(),
            db = get()
        ) as CafeteriaRepository
    }

    /** Account Repository */

    single {
        AccountRepositoryImpl(
            networkService = get(),
            db = get()
        ) as AccountRepository
    }

    /** Notice Repository */

    single {
        NoticeRepositoryImpl(
            context = get(),
            networkService = get()
        ) as NoticeRepository
    }


    /*****************************
     * Use Case
     *****************************/

    /** Activate Barcode */

    single {
        ActivateBarcode(
            accountService = get()
        )
    }

    /** Create Barcode */

    single {
        CreateBarcode()
    }

    /** Get Cafeteria */

    single {
        GetCafeteria(
            cafeteriaRepo = get()
        )
    }

    /** Get Cafeteria only */

    single {
        GetCafeteriaOnly(
            cafeteriaRepo = get()
        )
    }

    /** Get Cafeteria order */

    single {
        GetCafeteriaOrder(
            cafeteriaRepo = get()
        )
    }

    /** Set Cafeteria order */

    single {
        SetCafeteriaOrder(
            cafeteriaRepo = get()
        )
    }

    /** Reset Cafeteria order */

    single {
        ResetCafeteriaOrder(
            cafeteriaRepo = get()
        )
    }

    /** Get Version */

    single {
        GetVersion(
            versionRepo = get()
        )
    }

    /** Get Notice */

    single {
        GetNotice(
            noticeRepo = get()
        )
    }

    /** Login */

    single {
        Login(
            accountService = get()
        )
    }

    /** Remembered Login */

    single {
        RememberedLogin(
            accountService = get()
        )
    }

    /** Get Saved Account */

    single {
        GetSavedAccount(
            accountService = get()
        )
    }
}