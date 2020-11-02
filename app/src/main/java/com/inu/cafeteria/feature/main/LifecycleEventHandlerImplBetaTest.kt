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

package com.inu.cafeteria.feature.main

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.R
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.extension.fadeIn
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.usecase.SendAppFeedback
import com.inu.cafeteria.util.ShakeListener
import com.plattysoft.leonids.ParticleSystem
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class LifecycleEventHandlerImplBetaTest(
    private val context: Context
) : LifecycleEventHandler, KoinComponent {

    private val navigator: Navigator by inject()
    private val sendAppFeedback: SendAppFeedback by inject()

    override fun onCreate(activity: FragmentActivity) {
        if (activity !is MainActivity) {
            return
        }

        showWelcomeMessage(activity)
        showFeedbackButton(activity)
     }

    private fun showWelcomeMessage(activity: MainActivity) {
        Toast.makeText(activity, "테스트에 참여하여 주셔서 감사합니다!", Toast.LENGTH_SHORT).show()
    }

    private fun showFeedbackButton(activity: FragmentActivity) {
        Handler(Looper.getMainLooper()).postDelayed({
            activity.findViewById<ExtendedFloatingActionButton>(R.id.feedback_button)?.let {
                it.setOnClickListener { showFeedbackDialog(activity) }
                it.fadeIn(500)
            }
        }, 1000)
    }

    private fun showFeedbackDialog(activity: FragmentActivity) {
        navigator.showFeedbackDialog(activity) { feedbackMessage ->
            sendAppFeedback(feedbackMessage) { result ->
                result.onSuccess { body ->
                    welcomeUserWhoGaveUsFeedback(activity, body)
                }.onError {
                    handleError(it.message)
                }
            }
        }
    }

    private fun welcomeUserWhoGaveUsFeedback(activity: FragmentActivity, thanksMessage: String) {
        Toast.makeText(context, thanksMessage, Toast.LENGTH_SHORT).show()

        ParticleSystem(activity, 70, R.drawable.dot, 3000)
            .setSpeedRange(0.2f, 0.7f)
            .oneShot(activity.findViewById(R.id.main_pager), 70)
    }

    private fun handleError(errorMessage: String?) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onResume(activity: FragmentActivity) {
        // No shake detection!
    }

    override fun onPause(activity: FragmentActivity) {
        // No shake detection!
    }
}