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

package com.inu.cafeteria.common.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inu.cafeteria.R
import com.inu.cafeteria.common.extension.hideKeyboard
import com.inu.cafeteria.common.extension.requestFocusWithKeyboard
import com.inu.cafeteria.config.Config
import com.inu.cafeteria.entities.Notice
import com.inu.cafeteria.extension.withNonNull
import com.inu.cafeteria.feature.login.LoginActivity
import com.inu.cafeteria.feature.main.MainActivity
import com.inu.cafeteria.feature.order.AddOrderActivity
import com.inu.cafeteria.feature.reorder.CafeteriaReorderActivity
import com.plattysoft.leonids.ParticleSystem
import org.koin.core.KoinComponent
import timber.log.Timber

/**
 * Go everywhere.
 */

class Navigator(
    private val context: Context,
) : KoinComponent {

    fun showMain(extras: Bundle? = null) {
        startActivity(
            MainActivity.callingIntent(context).apply {
                extras?.let(::putExtras)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
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

    fun showAddWaitingOrder() {
        startActivity(
            AddOrderActivity.callingIntent(context)
        )
    }

    @SuppressLint("InflateParams")
    fun showNotice(activity: Activity, notice: Notice, onDismiss: () -> Unit) {
        val dialog = BottomSheetDialog(activity)
        val noticeView = activity.layoutInflater.inflate(R.layout.notice_view, null).apply {
            withNonNull(findViewById<TextView>(R.id.title)) {
                text = notice.title
            }

            withNonNull(findViewById<TextView>(R.id.body)) {
                text = notice.body
            }

            withNonNull(findViewById<Button>(R.id.dismiss)) {
                setOnClickListener {
                    dialog.dismiss()
                    onDismiss()
                }
            }
        }

        dialog.setContentView(noticeView)
        dialog.show()
    }

    fun showUpdate(activity: Activity) {
        AlertDialog
            .Builder(activity)
            .setTitle(context.getString(R.string.wait))
            .setMessage(context.getString(R.string.description_updated_needed))
            .setPositiveButton(context.getString(R.string.button_update)) { _, _ ->
                showStore()
            }
            .setCancelable(false) // Force!
            .show()
    }

    fun showOrderFinishedNotification(activity: Activity, title: String, body: String, onDismiss: () -> Unit = {}) {
        AlertDialog
            .Builder(activity)
            .setTitle(title)
            .setMessage(body)
            .setPositiveButton(context.getString(R.string.button_confirm)) { _, _ -> }
            .setOnDismissListener { onDismiss() }
            .show()
    }

    fun showTermsAndConditions() {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(Config.termsAndConditionsUrl))
        )
    }

    fun showDialog(activity: Activity, title: String, body: String, onDismiss: () -> Unit = {}) {
        AlertDialog
            .Builder(activity)
            .setTitle(title)
            .setMessage(body)
            .setPositiveButton(context.getString(R.string.button_confirm)) { _, _ -> }
            .setOnDismissListener { onDismiss() }
            .show()
    }

    fun showStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=" + Config.appId)
        }

        startActivity(intent)
    }

    @SuppressLint("RestrictedApi")
    fun showBetaTestFeedbackDialog(activity: Activity, sendFeedback: (String) -> Unit) {
        val textInput = EditText(activity).apply {
            hint = "관심 가져주셔서 감사합니다 :)"
        }

        AlertDialog.Builder(activity)
            .setTitle("피드백")
            .setMessage("개선이 필요한 부분을 알려주세요!\uD83D\uDE0A")
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

    @SuppressLint("RestrictedApi")
    fun showPotadosDialog(activity: Activity) {
        val potadosImageView = ImageView(activity).apply {
            setPadding(50, 50, 50, 50)
            setImageResource(R.drawable.potato)
            setOnClickListener {
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_once).apply {
                    duration = 200
                })
            }
        }

        AlertDialog.Builder(activity)
            .setTitle("감자 좋아하세요?")
            .setMessage("좋은 하루 보내세요 :)\n\nINU 카페테리아\n© potados99")
            .setView(potadosImageView, 60, 0, 60, 0)
            .setPositiveButton("닫기") { _, _ -> }
            .show()

        ParticleSystem(activity, 100, R.drawable.dot, 3000)
            .setSpeedRange(0.2f, 0.7f)
            .oneShot(potadosImageView, 100)

        Handler().postDelayed({
            potadosImageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_once).apply {
                duration = 200
            })
        }, 800)
    }

    fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.fail_no_activity), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startActivity(intent: Intent) {
        // Recent versions Android requires this flag
        // to start activity from non-activity context.
        context.startActivity(
            intent.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                Timber.i("Starting ${this.component?.className}.")
            }
        )
    }
}