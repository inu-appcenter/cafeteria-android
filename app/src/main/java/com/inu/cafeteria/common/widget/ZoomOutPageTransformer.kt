/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.common.widget

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max


/**
 * Thanks to Nitish Srivastava and Lavekush Agrawal.
 * https://stackoverflow.com/a/36057424
 */
class ZoomOutPageTransformer(val enableZoom: Boolean) : ViewPager.PageTransformer {

    private val minScale: Float = if (enableZoom) DEFAULT_SCALE else NO_SCALE
    private val minAlpha: Float = DEFAULT_ALPHA

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        var verticalMargin = pageHeight * (1 - minScale) / 2
        var horizontalMargin = pageWidth * (1 - minScale) / 2

        page.scaleX = minScale
        page.scaleY = minScale

        when  {
            (position < -1) -> {
                // [-Infinity,-1)
                // Page out of screen left
                page.alpha = minAlpha
                page.translationX = (horizontalMargin - verticalMargin / 2)
            }

            (position in (-1.0f..1.0f)) -> {
                // [-1,1]
                // Affected area
                val scaleFactor = max(minScale, 1 - abs(position))

                verticalMargin = pageHeight * (1 - scaleFactor) / 2
                horizontalMargin = pageWidth * (1 - scaleFactor) / 2

                page.translationX = when (position < 0) {
                    true -> horizontalMargin - (verticalMargin / 2)
                    false -> -horizontalMargin + (verticalMargin / 2)
                }

                page.scaleX = scaleFactor
                page.scaleY = scaleFactor

                // Fade the page relative to its size.
                page.alpha = minAlpha + ((scaleFactor - minScale) / (1 - minScale) * (1 - minAlpha))
            }

            (position > 1) -> {
                // (1,+Infinity]
                // Page out of screen right
                page.alpha = minAlpha
                page.translationX = (horizontalMargin - verticalMargin / 2)
            }
            else -> {
                // NO REACH
            }
        }
    }

    companion object {
        private const val NO_SCALE = 1.0f
        private const val DEFAULT_SCALE = 0.85f
        private const val DEFAULT_ALPHA = 0.7f
    }
}