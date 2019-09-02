package org.inu.cafeteria.repository

import org.inu.cafeteria.BuildConfig
import org.inu.cafeteria.model.scheme.Version
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call

class VersionRepositoryImpl(
    private val networkService: CafeteriaNetworkService
) : VersionRepository() {
    override fun getCurrentVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getLatestVersion(): Call<Version> {
        return networkService.getVersionResult()
    }
}