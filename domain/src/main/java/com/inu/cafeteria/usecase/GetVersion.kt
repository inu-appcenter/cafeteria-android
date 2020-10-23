/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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