package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.Version

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