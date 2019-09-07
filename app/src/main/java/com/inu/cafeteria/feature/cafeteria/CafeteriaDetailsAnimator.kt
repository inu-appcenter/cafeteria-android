/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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


