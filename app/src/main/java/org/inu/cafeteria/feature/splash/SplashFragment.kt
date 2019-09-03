package org.inu.cafeteria.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.handleRetrofitException
import org.inu.cafeteria.common.util.ThemedDialog
import org.inu.cafeteria.usecase.GetVersion
import org.koin.android.ext.android.inject
import timber.log.Timber

class SplashFragment : BaseFragment() {

    private val getVersion: GetVersion by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Do this after everything done.
        checkVersion()
    }

    private fun checkVersion() {
        getVersion(Unit) {
            it.onSuccess {
                Timber.i("Current version is ${it.currentVersion}, latest is ${it.latestVersion}, so update needed? ${it.needUpdate()}.")

                ThemedDialog(context!!)
                    .withTitle(R.string.dialog_new_version)
                    .withMessage(R.string.dialog_ask_update)
                    .withPositiveButton(R.string.button_update) {
                        // TODO Go update
                    }
                    .withNegativeButton(R.string.button_cancel)

            }.onError(::handleRetrofitException)
        }
    }
}