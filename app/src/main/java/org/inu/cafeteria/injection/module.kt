package org.inu.cafeteria.injection

import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.repository.VersionRepository
import org.inu.cafeteria.repository.VersionRepositoryImpl
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

    /** Version Repository */
    single {
        VersionRepositoryImpl(
            networkService = get()
        ) as VersionRepository
    }

    /** Get Version */
    single {
        GetVersion(
            versionRepo = get()
        )
    }
}