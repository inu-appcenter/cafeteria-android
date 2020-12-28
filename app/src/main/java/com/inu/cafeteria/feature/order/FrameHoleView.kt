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

package com.inu.cafeteria.feature.order

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.inu.cafeteria.R

class FrameHoleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var holeMargin: Float
    private var holeRatio: String
    private var holeBorderRadius: Float

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.FrameHoleView, 0, 0).apply {
            holeMargin = getDimensionPixelSize(R.styleable.FrameHoleView_holeMargin, 0).toFloat()
            holeRatio = getString(R.styleable.FrameHoleView_holeRatio) ?: "0.75"
            holeBorderRadius = getFloat(R.styleable.FrameHoleView_holeBorderRadius, 8f)
        }
    }

    private val holeWidth: Float
        get() {
            return width.toFloat() - (2*holeMargin)
        }
    private val holeHeight: Float
        get() {
            return holeWidth / holeRatio.toFloat()
        }

    private val eraser = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val stroke = Paint().apply {
        isAntiAlias = true
        strokeWidth = 4f
        color = Color.WHITE
        style = Paint.Style.STROKE
    }

    private val rect = RectF()

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBorder(canvas)
        drawHole(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        path.rewind()
        path.addRoundRect(
            rect.apply {
                setRect(-28f + 1f/*border*/)
            },
            holeBorderRadius,
            holeBorderRadius,
            Path.Direction.CW
        )

        canvas.drawPath(path, stroke)
    }

    private fun drawHole(canvas: Canvas) {
        canvas.drawRoundRect(
            rect.apply {
                setRect(-28f)
            },
            holeBorderRadius, holeBorderRadius, eraser
        )

    }

    private fun setRect(offset: Float = 0f) {
        rect.set(
            ((width - holeWidth)/2) - offset,
            ((height - holeHeight)/2) - offset,
            ((width - holeWidth)/2 + holeWidth) + offset,
            ((height - holeHeight)/2 + holeHeight) + offset
        )
    }
}