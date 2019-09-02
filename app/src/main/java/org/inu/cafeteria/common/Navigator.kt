package org.inu.cafeteria.common

import android.content.Context
import android.content.Intent
import org.inu.cafeteria.base.FailableComponent
import org.inu.cafeteria.feature.login.LoginActivity
import org.inu.cafeteria.feature.main.CafeteriaActivity
import org.inu.cafeteria.feature.splash.SplashActivity
import org.koin.core.KoinComponent
import timber.log.Timber

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