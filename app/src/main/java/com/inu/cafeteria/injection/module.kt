package com.inu.cafeteria.injection

import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.feature.cafeteria.CafeteriaDetailsAnimator
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

    /** Cafeteria Details Animator */
    single {
        CafeteriaDetailsAnimator()
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