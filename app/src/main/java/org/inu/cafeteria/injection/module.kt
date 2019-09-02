package org.inu.cafeteria.injection

import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.repository.*
import org.inu.cafeteria.usecase.GetVersion
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
        )
    }

    /** Student Info Repository */
    single {
        StudentInfoRepositoryImpl(
            context = get()
        )
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

    /** Get Version */
    single {
        GetVersion(
            versionRepo = get()
        )
    }
}