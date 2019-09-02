package org.inu.cafeteria.model

data class VersionCompared(val currentVersion: String, val latestVersion: String) {
    fun needUpdate(): Boolean {
        return latestVersion > currentVersion
    }
    fun isUpToDate(): Boolean {
        return latestVersion <= currentVersion
    }
}