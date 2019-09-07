package org.inu.cafeteria.common

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import org.inu.cafeteria.R
import org.inu.cafeteria.base.FailableComponent
import org.inu.cafeteria.common.widget.ThemedDialog
import org.inu.cafeteria.feature.cafeteria.CafeteriaDetailsActivity
import org.inu.cafeteria.feature.login.LoginActivity
import org.inu.cafeteria.feature.main.MainActivity
import org.inu.cafeteria.feature.splash.SplashActivity
import org.inu.cafeteria.model.json.Cafeteria
import org.koin.core.KoinComponent
import timber.log.Timber
import kotlin.system.exitProcess
import android.net.Uri
import org.inu.cafeteria.BuildConfig


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
            MainActivity.callingIntent(context)
        )
    }

    fun showServerDeadDialog(activity: FragmentActivity) {
        ThemedDialog(activity)
            .withTitle(R.string.title_server_error)
            .withMessage(R.string.dialog_server_not_respond)
            .withPositiveButton(R.string.button_exit) { exitProcess(0) }
            .show()
    }

    fun showCafeteriaDetail(activity: FragmentActivity, cafeteria: Cafeteria, sharedImageView: ImageView, sharedTextView: TextView) {
        val intent = CafeteriaDetailsActivity.callingIntent(activity, cafeteria)

        val imageAnim = Pair.create(sharedImageView as View, sharedImageView.transitionName)
        val titleAnim = Pair.create(sharedTextView as View, sharedTextView.transitionName)

        val activityOptions = ActivityOptionsCompat
            .makeSceneTransitionAnimation(activity, imageAnim, titleAnim)
        activity.startActivity(intent, activityOptions.toBundle())
    }

    fun showStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
        }

        startActivity(intent)
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