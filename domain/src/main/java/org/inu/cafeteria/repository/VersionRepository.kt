package org.inu.cafeteria.repository

import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.model.scheme.LoginParams
import org.inu.cafeteria.model.scheme.LoginResult
import org.inu.cafeteria.model.scheme.Version
import retrofit2.Call

abstract class VersionRepository : Repository() {
    abstract fun getCurrentVersion(): String
    abstract fun getLatestVersion(callback: DataCallback<Version>)
}