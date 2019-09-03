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
import org.inu.cafeteria.usecase.GetVersion
import org.inu.cafeteria.util.Notify
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Display splash screen, while checking version.
 * Route activity launches this activity by default.
 */
class SplashFragment : BaseFragment() {

    private val getVersion: GetVersion by inject()
    private val navigator: Navigator by inject()

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
            onUpdate = {
                Notify(context).short(getString(R.string.notify_not_implemented))
            },
            onDismiss = {
                // pass
            },
            finally = {
                navigator.showLogin()
                finishActivity()
            }
        )
    }

    /**
     * Compare build version to the latest version(from the server).
     * If update is needed, let user know it.
     *
     * @param onUpdate launched when user pressed update button.
     * @param onDismiss launched when user pressed cancel button.
     * @param fainally launched after check routine.
     * This is blocked until the user takes action or no need to update, or error occurred.
     *
     * WARNING: Do not finish activity on [onUpdate] and [finally].
     * It will prevent [finally] being executed.
     */
    private fun checkVersion(
        onUpdate: () -> Unit,
        onDismiss: () -> Unit,
        finally: () -> Unit
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
                                finally()
                            }
                            .withNegativeButton(R.string.button_cancel) {
                                onDismiss()
                                finally()
                            }
                            .show()
                    }
                    else -> {
                        // No need to update. Pass.
                        finally()
                    }
                }
            }.onError { e ->
                // Request failed.
                handleRetrofitException(e)
                finally()
            }
        }

        /* NO REACH */
    }
}
