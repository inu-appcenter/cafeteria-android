package org.inu.cafeteria.repository

import org.inu.cafeteria.BuildConfig
import org.inu.cafeteria.extension.onResult
import org.inu.cafeteria.model.scheme.Version
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call

class VersionRepositoryImpl(
    private val networkService: CafeteriaNetworkService
) : VersionRepository() {

    override fun getCurrentVersion(): String {
        // BuildConfig from module common.
        return BuildConfig.VERSION_NAME
    }

    override fun getLatestVersion(callback: DataCallback<Version>) {
        networkService.getVersionResult().onResult(
            async = callback.async,
            onSuccess = callback.onSuccess,
            onFail = callback.onFail
        )
    }
}