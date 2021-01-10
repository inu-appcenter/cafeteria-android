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

package com.inu.cafeteria.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater

class AsyncFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
) {
    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    private var isInflated = false
    private var pendingActions: MutableList<AsyncFrameLayout.() -> Unit> = ArrayList()

    fun inflateAsync(layoutResId: Int) {
        AsyncLayoutInflater(context).inflate(layoutResId, this) { view, _, _ ->
            addView(view)
            isInflated = true
            pendingActions.forEach { action -> action() }
            pendingActions.clear()
        }
    }

    fun invokeWhenInflated(action: AsyncFrameLayout.() -> Unit) {
        if (isInflated) {
            action()
        } else {
            pendingActions.add(action)
        }
    }
}