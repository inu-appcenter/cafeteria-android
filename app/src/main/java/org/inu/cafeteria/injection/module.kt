package org.inu.cafeteria.injection

import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.parser.CafeteriaParser
import org.inu.cafeteria.parser.FoodMenuParser
import org.inu.cafeteria.repository.*
import org.inu.cafeteria.usecase.*
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
            networkService = get()
        ) as VersionRepository
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