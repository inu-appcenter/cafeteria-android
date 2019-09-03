package org.inu.cafeteria.common

import android.content.Context
import android.content.Intent
import org.inu.cafeteria.R
import org.inu.cafeteria.base.FailableComponent
import org.inu.cafeteria.common.extension.baseActivity
import org.inu.cafeteria.common.util.ThemedDialog
import org.inu.cafeteria.feature.login.LoginActivity
import org.inu.cafeteria.feature.main.CafeteriaActivity
import org.inu.cafeteria.feature.splash.SplashActivity
import org.koin.core.KoinComponent
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * Go everywhere.
 */
class Navigator(
    private val context: Context
) : FailableComponent(), KoinComponent {

    fun showSplash() {
        startActivity(
            SplashActivity.callingIntent(context)
        )
    }

    fun showLogin() {
        startActivity(
            LoginActivity.callingIntent(context)
        )
    }

    fun showMain() {
        startActivity(
            CafeteriaActivity.callingIntent(context)
        )
    }

    fun showServerDeadDialog() {
        ThemedDialog(context)
            .withTitle(R.string.title_server_error)
            .withMessage(R.string.dialog_server_not_respond)
            .withPositiveButton(R.string.button_exit) { exitProcess(0) }
            .show()
    }

    private fun startActivity(intent: Intent) {
        // Recent versions Android requires this flag
        // to start activity from non-activity context.
        context.startActivity(
            intent.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                Timber.i("Starting ${this.component?.className}.")
            }
        )
    }
}