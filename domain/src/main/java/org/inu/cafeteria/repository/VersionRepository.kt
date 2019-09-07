package org.inu.cafeteria.repository

import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.model.scheme.Version
import retrofit2.Call

abstract class VersionRepository : Repository() {
    /**
     * Dismiss update popup of this version.
     */
    abstract fun dismissVersion(version: String)

    /**
     * Get last dismissed version.
     */
    abstract fun getDismissedVersion(): String?

    abstract fun getCurrentVersion(): String
    abstract fun getLatestVersion(callback: DataCallback<Version>)
}