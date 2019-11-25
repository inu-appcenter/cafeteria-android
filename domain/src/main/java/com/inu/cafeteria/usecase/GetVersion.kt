/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.usecase

import com.inu.cafeteria.functional.Result
import com.inu.cafeteria.interactor.UseCase
import com.inu.cafeteria.model.VersionCompared
import com.inu.cafeteria.model.scheme.Version
import com.inu.cafeteria.repository.Repository
import com.inu.cafeteria.repository.VersionRepository

class GetVersion(
    private val versionRepo: VersionRepository
) : UseCase<Unit, VersionCompared>() {

    /**
     * We want to know the current version and the latest version.
     * This use case returns [VersionCompared] object if succeeded.
     */
    override fun run(params: Unit): Result<VersionCompared> = Result.of {
        var versionResult: Version? = null
        var failure: Exception? = null

        versionRepo.getLatestVersion(
            Repository.DataCallback(
                async = false,
                onSuccess = { versionResult = it },
                onFail = { failure = it }
            )
        )

        failure?.let { throw it }

        val currentVersion = versionRepo.getCurrentVersion()

        return@of VersionCompared(
            currentVersion = currentVersion,
            latestVersion = versionResult!!.android.latest
        )
    }
}