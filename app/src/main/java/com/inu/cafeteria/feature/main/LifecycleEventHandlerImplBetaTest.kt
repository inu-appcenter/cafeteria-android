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
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.common.LifecycleEventHandler
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.usecase.SendAppFeedback
import com.inu.cafeteria.util.ShakeListener
import org.koin.core.KoinComponent
import org.koin.core.inject

class LifecycleEventHandlerImplBetaTest(
    private val context: Context
) : LifecycleEventHandler, KoinComponent {

    private val navigator: Navigator by inject()
    private val sendAppFeedback: SendAppFeedback by inject()

    private val shakeListener = ShakeListener(context)

    override fun onCreate(activity: FragmentActivity) {
        if (activity !is MainActivity) {
            return
        }

        Toast.makeText(context, BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "테스트에 참여하여 주셔서 감사합니다!", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "기기를 흔들어 피드백을 보내실 수 있습니다 :)", Toast.LENGTH_LONG).show()
    }

    override fun onResume(activity: FragmentActivity) {
        shakeListener.setOnShakeListener { onShakeDetected(activity) }
        shakeListener.resume()
    }

    override fun onPause(activity: FragmentActivity) {
        shakeListener.pause()
    }

    private fun onShakeDetected(activity: FragmentActivity) {
        navigator.showFeedbackDialog(activity) { feedbackMessage ->
            sendAppFeedback(feedbackMessage) { result ->
                result.onSuccess { body ->
                    Toast.makeText(context, body, Toast.LENGTH_SHORT).show()
                }.onError {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}