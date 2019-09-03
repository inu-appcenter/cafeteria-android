package org.inu.cafeteria.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.inu.cafeteria.R
import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.baseActivity
import org.inu.cafeteria.common.extension.finishActivity
import org.inu.cafeteria.common.extension.handleRetrofitException
import org.inu.cafeteria.common.util.ThemedDialog
import org.inu.cafeteria.exception.ServerNoResponseException
import org.inu.cafeteria.usecase.GetVersion
import org.inu.cafeteria.util.Notify
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Display splash screen, while checking version.
 * Route activity launches this activity by default.
 */
class SplashFragment : BaseFragment() {

    private val getVersion: GetVersion by inject()
    private val navigator: Navigator by inject()

    init {
        failables += this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkVersion(
            onFail = {
                if (it is ServerNoResponseException) {
                    // server fatal error.
                    ThemedDialog(baseActivity!!)
                        .withTitle(R.string.title_server_error)
                        .withMessage(R.string.dialog_server_not_respond)
                        .withPositiveButton(R.string.button_exit) {
                            exitProcess(0)
                        }
                        .show()
                } else {
                    handleRetrofitException(it)
                }
            },
            onPass = {
                navigator.showLogin()
                finishActivity()
            },
            onUpdate = {
                Notify(context).short(getString(R.string.notify_not_implemented))
                navigator.showLogin()
                finishActivity()
            },
            onDismiss = {
                navigator.showLogin()
                finishActivity()
            }
        )
    }

    /**
     * Compare build version to the latest version(from the server).
     * If update is needed, let user know it.
     *
     * @param onFail launched on server fault.
     * @param onPass launched when no need to update.
     * @param onUpdate launched when user pressed update button.
     * @param onDismiss launched when user pressed cancel button.
     *
     */
    private fun checkVersion(
        onFail: (e: Exception) -> Unit,
        onPass: () -> Unit,
        onUpdate: () -> Unit,
        onDismiss: () -> Unit
    ) {
        getVersion(Unit) {
            it.onSuccess { version ->
                when (version.needUpdate()) {
                    true -> {
                        // New version detected.
                        ThemedDialog(baseActivity!!)
                            .withTitle(R.string.dialog_new_version)
                            .withMessage(R.string.dialog_ask_update)
                            .withPositiveButton(R.string.button_update) {
                                onUpdate()
                            }
                            .withNegativeButton(R.string.button_cancel) {
                                onDismiss()
                            }
                            .show()
                    }
                    else -> {
                        onPass()
                    }
                }
            }.onError { e ->
                onFail(e)
            }
        }

        /* NO REACH */
    }
}
