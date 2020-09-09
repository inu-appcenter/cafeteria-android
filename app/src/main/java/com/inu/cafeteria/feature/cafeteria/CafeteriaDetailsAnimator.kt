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

package com.inu.cafeteria.feature.cafeteria

import android.transition.Fade
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.inu.cafeteria.base.FailableComponent
import com.inu.cafeteria.common.extension.cancelTransition

class CafeteriaDetailsAnimator : FailableComponent() {

    private val TRANSITION_DELAY = 200L
    private val TRANSITION_DURATION = 400L

    private val SCALE_UP_VALUE = 1.0F
    private val SCALE_UP_DURATION = 400L

    private val SCALE_DOWN_VALUE = 0.0F
    private val SCALE_DOWN_DURATION = 200L

    internal fun postponeEnterTransition(activity: FragmentActivity) = activity.postponeEnterTransition()
    internal fun cancelTransition(view: View) = view.cancelTransition()

    internal fun scaleUpView(view: View) = scaleView(view, SCALE_UP_VALUE, SCALE_UP_VALUE, SCALE_UP_DURATION)
    internal fun scaleDownView(view: View) = scaleView(view, SCALE_DOWN_VALUE, SCALE_DOWN_VALUE, SCALE_DOWN_DURATION)

    internal fun fadeVisible(viewContainer: ViewGroup, view: View) = beginTransitionFor(viewContainer, view, View.VISIBLE)
    internal fun fadeInvisible(viewContainer: ViewGroup, view: View) = beginTransitionFor(viewContainer, view, View.INVISIBLE)

    private fun scaleView(view: View, x: Float, y: Float, duration: Long) =
            view.animate()
                    .scaleX(x)
                    .scaleY(y)
                    .setDuration(duration)
                    .setInterpolator(FastOutSlowInInterpolator())
                    .withLayer()
                    .setListener(null)
                    .start()

    private fun beginTransitionFor(viewContainer: ViewGroup, view: View, visibility: Int) {
        val transition = Fade()
        transition.startDelay = TRANSITION_DELAY
        transition.duration = TRANSITION_DURATION
        TransitionManager.beginDelayedTransition(viewContainer, transition)
        view.visibility = visibility
    }
}


