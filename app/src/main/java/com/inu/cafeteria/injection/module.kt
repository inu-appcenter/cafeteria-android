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

import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.parser.CafeteriaParser
import com.inu.cafeteria.parser.FoodMenuParser
import com.inu.cafeteria.repository.*
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


    /*****************************
     * Repository
     *****************************/

    /** Cafeteria Repository */

    single {
        CafeteriaRepositoryImpl(
            networkService = get(),
            cafeteriaParser = get(),
            foodMenuParser = get()
        ) as CafeteriaRepository
    }

    /** Login Repository */

    single {
        LoginRepositoryImpl(
            networkService = get()
        ) as LoginRepository
    }

    /** Private Repository */

    single {
        PrivateRepositoryImpl() as PrivateRepository
    }

    /** Student Info Repository */

    single {
        StudentInfoRepositoryImpl(
            context = get(),
            networkService = get()
        ) as StudentInfoRepository
    }

    /** Version Repository */

    single {
        VersionRepositoryImpl(
            context = get(),
            networkService = get()
        ) as VersionRepository
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
            studentInfoRepo = get()
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

    /** Get Food Menu */

    single {
        GetFoodMenu(
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
            loginRepo = get()
        )
    }

    /** Logout */

    single {
        Logout(
            loginRepo = get()
        )
    }


    /*****************************
     * Parser
     *****************************/

    /** Cafeteria Parser */

    single {
        CafeteriaParser()
    }

    /** FoodMenu Parser */

    single {
        FoodMenuParser()
    }
}