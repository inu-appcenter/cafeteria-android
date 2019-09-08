/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.R
import com.inu.cafeteria.base.FailableComponent
import com.inu.cafeteria.common.widget.ThemedDialog
import com.inu.cafeteria.feature.barcode.BarcodeActivity
import com.inu.cafeteria.feature.cafeteria.CafeteriaDetailsActivity
import com.inu.cafeteria.feature.info.InfoActivity
import com.inu.cafeteria.feature.login.LoginActivity
import com.inu.cafeteria.feature.main.MainActivity
import com.inu.cafeteria.feature.splash.SplashActivity
import com.inu.cafeteria.model.json.Cafeteria
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

    fun showFatalDialog(activity: FragmentActivity, e: Exception) {
        ThemedDialog(activity)
            .withTitle(R.string.title_fatal_error)
            .withMessage(e.localizedMessage)
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

    fun showUrl(url: String) {
        startActivity(
            Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
        )
    }

    fun showInfo() {
        startActivity(
            InfoActivity.callingIntent(context)
        )
    }

    fun showBarcode() {
        startActivity(
            BarcodeActivity.callingIntent(context)
        )
    }

    fun requestSettingsPermission() {
        startActivity(
            Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            }
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