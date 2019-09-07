package org.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import org.inu.cafeteria.BuildConfig
import org.inu.cafeteria.extension.onResult
import org.inu.cafeteria.model.Cache
import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.model.scheme.Version
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call
import timber.log.Timber

class VersionRepositoryImpl(
    private val context: Context,
    private val networkService: CafeteriaNetworkService
) : VersionRepository() {

    private val pref = context.getSharedPreferences(
        PREFERENCE_VERSION_INFO,
        Activity.MODE_PRIVATE
    )

    private val versionCache = Cache<Version>()

    override fun dismissVersion(version: String) {
        pref.edit(true) {
            putString(KEY_DISMISSED_VERSION, version)
        }
    }

    override fun getDismissedVersion(): String? {
        return pref.getString(KEY_DISMISSED_VERSION, EMPTY)
    }

    override fun getCurrentVersion(): String {
        // BuildConfig from module common.
        return BuildConfig.VERSION_NAME
    }

    override fun getLatestVersion(callback: DataCallback<Version>) {
        if (versionCache.isValid) {
            versionCache.get()?.let {
                callback.onSuccess(it)
                Timber.i("Got latest version info from cache!")
                return
            }
        }

        networkService.getVersionResult().onResult(
            async = callback.async,
            onSuccess = {
                callback.onSuccess(it)
                versionCache.set(it)
            },
            onFail = callback.onFail
        )
    }


    companion object {
        private const val PREFERENCE_VERSION_INFO = "versionInfo"

        private const val KEY_DISMISSED_VERSION = "dismissedVersion"

        private const val EMPTY = ""
    }
}