package org.inu.cafeteria.usecase

import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.functional.Result
import org.inu.cafeteria.interactor.UseCase
import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.model.scheme.LogoutResult
import org.inu.cafeteria.model.scheme.Version
import org.inu.cafeteria.repository.Repository
import org.inu.cafeteria.repository.VersionRepository
import java.io.IOException

/**
 * IOException: Server failure
 * RuntimeException: Unexpected failure
 */
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