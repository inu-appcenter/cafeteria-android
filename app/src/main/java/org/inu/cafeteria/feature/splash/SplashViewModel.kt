package org.inu.cafeteria.feature.splash

import android.app.Activity
import android.content.Context
import org.inu.cafeteria.R
import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.common.extension.finishActivity
import org.inu.cafeteria.common.extension.defaultDataErrorHandle
import org.inu.cafeteria.common.widget.ThemedDialog
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.repository.VersionRepository
import org.inu.cafeteria.usecase.GetVersion
import org.inu.cafeteria.util.Notify
import org.koin.core.inject
import timber.log.Timber

class SplashViewModel : BaseViewModel() {

    private val getVersion: GetVersion by inject()
    private val navigator: Navigator by inject()

    private val versionRepo: VersionRepository by inject()

    var onServerNoResponse: () -> Unit = {}

    init {
        failables += this
        failables += getVersion
        failables += navigator
        failables += versionRepo
    }

    /**
     * Compare build version to the latest version(from the server).
     * If update is needed, let user know it.
     *
     * @param onFail launched on server fault.
     * @param onPass launched when no need to update.
     * @param onUpdate launched when user pressed update button.
     * @param onDismiss launched when user pressed cancel button.
     */
    fun tryCheckVersion(
        activity: Activity,
        onFail: (e: Exception) -> Unit,
        onPass: () -> Unit,
        onUpdate: () -> Unit,
        onDismiss: () -> Unit
    ) {

        val getDialog = { version: VersionCompared ->
            ThemedDialog(activity)
                .withTitle(R.string.dialog_new_version, version.latestVersion)
                .withMessage(R.string.dialog_ask_update)
                .withCheckBox(R.string.desc_pass_this_version)
                .withPositiveButton(R.string.button_update) { dismiss ->
                    if (dismiss) {
                        versionRepo.dismissVersion(version.latestVersion)
                    }
                    onUpdate()
                }
                .withNegativeButton(R.string.button_cancel) { dismiss ->
                    if (dismiss) {
                        versionRepo.dismissVersion(version.latestVersion)
                    }
                    onDismiss()
                }
        }

        getVersion(Unit) {
            it.onSuccess { versionCompared ->

                Timber.i("Current version: ${versionCompared.currentVersion}")
                Timber.i("Latest version: ${versionCompared.latestVersion}")
                Timber.i("Ignored update: ${versionRepo.getDismissedVersion()}")

                val thisVersionIsIgnoredByUser = versionRepo.getDismissedVersion() == versionCompared.latestVersion

                if (versionCompared.needUpdate() && !thisVersionIsIgnoredByUser) {
                    getDialog(versionCompared).show()
                } else {
                    onPass()
                }

            }.onError { e ->
                onFail(e)
            }
        }
    }

    /**
     * ServerNoResponseException is a special case, so it need to be
     * handled carefully.
     */
    fun handleVersionCheckFailure(e: Exception) {
        when (e) {
            is ServerNoResponseException -> onServerNoResponse()
            else -> {
                defaultDataErrorHandle(e)
            }
        }
    }

    /**
     * Navigate to LoginActivity and close current activity.
     */
    fun showLogin(fragment: BaseFragment) {
        navigator.showLogin()
        fragment.finishActivity()
    }

    fun goUpdate() {
        navigator.showStore()
        Timber.i("User is gone to the store.")
    }
}