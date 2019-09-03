package org.inu.cafeteria.feature.splash

import android.content.Context
import org.inu.cafeteria.R
import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.common.extension.baseActivity
import org.inu.cafeteria.common.extension.finishActivity
import org.inu.cafeteria.common.extension.handleRetrofitException
import org.inu.cafeteria.common.util.ThemedDialog
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.model.VersionCompared
import org.inu.cafeteria.usecase.GetVersion
import org.inu.cafeteria.util.Notify
import org.koin.android.ext.android.inject
import org.koin.core.inject

class SplashViewModel : BaseViewModel() {

    private val context: Context by inject()
    private val getVersion: GetVersion by inject()
    private val navigator: Navigator by inject()

    init {
        failables += this
        failables += getVersion
        failables += navigator
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
        onFail: (e: Exception) -> Unit,
        onPass: () -> Unit,
        onUpdate: () -> Unit,
        onDismiss: () -> Unit
    ) {

        val getDialog = { version: VersionCompared ->
            ThemedDialog(context)
                .withTitle(R.string.dialog_new_version, version.latestVersion)
                .withMessage(R.string.dialog_ask_update)
                .withPositiveButton(R.string.button_update, onUpdate)
                .withNegativeButton(R.string.button_cancel, onDismiss)
        }

        getVersion(Unit) {
            it.onSuccess { version ->
                if (version.needUpdate()) {
                    getDialog(version).show()
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
            is ServerNoResponseException -> navigator.showServerDeadDialog()
            else -> {
                handleRetrofitException(e)
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
        Notify(context).short(R.string.notify_not_implemented)
    }
}