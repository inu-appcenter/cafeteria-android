package com.inu.cafeteria.model

/**
 * A class having a current version and a latest version.
 * It is useful when we want to get and compare versions in one place.
 */
data class VersionCompared(val currentVersion: String, val latestVersion: String) {
    fun needUpdate(): Boolean {
        return !isUpToDate()
    }
    fun isUpToDate(): Boolean {
        return latestVersion <= currentVersion
    }
}