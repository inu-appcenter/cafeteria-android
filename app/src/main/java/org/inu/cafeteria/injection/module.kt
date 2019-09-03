package org.inu.cafeteria.injection

import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.repository.*
import org.inu.cafeteria.usecase.ActivateBarcode
import org.inu.cafeteria.usecase.GetVersion
import org.inu.cafeteria.usecase.Login
import org.inu.cafeteria.usecase.Logout
import org.koin.dsl.module

val myModules = module {

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

    /** Login Repository */
    single {
        LoginRepositoryImpl(
            networkService = get()
        ) as LoginRepository
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
}