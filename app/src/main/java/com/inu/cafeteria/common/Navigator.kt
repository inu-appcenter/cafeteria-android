/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.R
import com.inu.cafeteria.common.extension.hideKeyboard
import com.inu.cafeteria.common.extension.requestFocusWithKeyboard
import com.inu.cafeteria.common.widget.ThemedDialog
import com.inu.cafeteria.feature.login.LoginActivity
import com.inu.cafeteria.feature.main.MainActivity
import com.inu.cafeteria.feature.reorder.CafeteriaReorderActivity
import org.koin.core.KoinComponent
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * Go everywhere.
 */

class Navigator(
    private val context: Context
) : KoinComponent {

    fun showMain() {
        startActivity(
            MainActivity.callingIntent(context)
        )
    }

    fun showLogin() {
        startActivity(
            LoginActivity.callingIntent(context)
        )
    }

    fun showSorting() {
        startActivity(
            CafeteriaReorderActivity.callingIntent(context)
        )
    }

    @SuppressLint("RestrictedApi")
    fun showFeedbackDialog(activity: FragmentActivity, sendFeedback: (String) -> Unit) {
        val textInput = EditText(activity).apply {
            hint = "피드백을 작성해주세요 :)"
        }

        AlertDialog.Builder(activity)
            .setTitle("피드백")
            .setMessage("개선이 필요한 부분을 알려주세요!")
            .setView(textInput, 60, 0, 60, 0)
            .setPositiveButton("보내기") { dialog, _ ->
                sendFeedback(textInput.text.toString())
                textInput.hideKeyboard()
                dialog.dismiss() // Not gonna dismiss automatically
            }
            .setNegativeButton("취소하기") { _, _ ->
                textInput.hideKeyboard()
            }
            .show()
            .setCanceledOnTouchOutside(false)

        textInput.requestFocusWithKeyboard()
    }

    /**
     * Explain user that the server or network is down.
     * Only choose is to close the app.
     */

    fun showNoConnectionDialog(activity: FragmentActivity) {
        ThemedDialog(activity)
            .withTitle(R.string.title_server_error)
            .withMessage(R.string.dialog_server_not_respond)
            .withPositiveButton(R.string.button_exit) { exitProcess(0) }
            .show()
    }

    /**
     * Explain user that the server or network is down.
     * Give user a chance to retry.
     */

    fun showNoConnectionDialog(activity: FragmentActivity, onRetry: () -> Unit) {
        ThemedDialog(activity)
            .withTitle(R.string.title_server_error)
            .withMessage(R.string.dialog_server_not_respond)
            .withPositiveButton(R.string.button_retry) { onRetry() }
            .show()
    }

    fun showFatalDialog(activity: FragmentActivity, e: Exception) {
        ThemedDialog(activity)
            .withTitle(R.string.title_fatal_error)
            .withMessage(e.localizedMessage)
            .withPositiveButton(R.string.button_exit) { exitProcess(0) }
            .show()
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

    /* TODO
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
*/

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