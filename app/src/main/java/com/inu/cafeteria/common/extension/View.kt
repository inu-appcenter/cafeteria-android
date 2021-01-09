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

package com.inu.cafeteria.common.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.inu.cafeteria.R
import it.sephiroth.android.library.xtooltip.ClosePolicy
import it.sephiroth.android.library.xtooltip.Tooltip

fun View.cancelTransition() {
    transitionName = null
}

fun View.setBackgroundTint(color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

fun View.setPadding(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    setPadding(
        left ?: paddingLeft,
        top ?: paddingTop,
        right ?: paddingRight,
        bottom ?: paddingBottom
    )
}

var View.isVisible: Boolean
    get() = this.visibility == View.VISIBLE
    set(visible) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

fun View.setVisible(visible: Boolean, invisible: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else invisible
}

fun <T : View> T?.withinAlphaAnimation(
    from: Float,
    to: Float,
    delay: Long = 0,
    action: T?.() -> Unit = {}
) {
    this?.let {
        alpha = from
    }

    action(this)

    this?.let {
        Handler(Looper.getMainLooper()).postDelayed({ animate().alpha(to) }, delay)
    }
}

fun View.slideInWithFade(directionVector: Int) {
    alpha = 0f
    x += -30f * directionVector
    animate().alpha(1f).x(0f)
}

fun View.removeFromParent() {
    (parent as? ViewGroup)?.removeView(this)
}

fun View.fadeIn(duration: Long = 500L) {
    alpha = 0f
    setVisible(true)
    animate().alpha(1f).setDuration(duration).start()
}

fun View.fadeOut(duration: Long = 500L) {
    alpha = 1f
    animate().alpha(0f).setDuration(duration).withEndAction {
        setVisible(false)
    }.start()
}

fun View.fadeInAndAnimateMargin(@DimenRes margin: Int, duration: Long) {
    val marginPixels = resources.getDimensionPixelSize(margin)

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            alpha = interpolatedTime

            updateLayoutParams<ConstraintLayout.LayoutParams> {
                val marginInPixels = (marginPixels * interpolatedTime).toInt()
                setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)
            }
        }
    }

    alpha = 0f
    setVisible(true)

    startAnimation(animation.apply {
        setDuration(duration)
    })
}

fun View.forceRippleAnimation() {
    val background: Drawable = background

    background.state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)

    Handler().postDelayed({
        background.state = intArrayOf()
    }, 200)
}

fun View.showTooltip(context: Context, rootView: View, gravity: Tooltip.Gravity, @StringRes text: Int, onDismiss: () -> Unit = {}) {
    (getTag(R.id.tooltip) as? Tooltip)?.dismiss()

    post {
        // We need to put this code in the View.post().
        // Because it "seems that you set the tooltip too early on the lifecycle" - Sulfkain
        // More information: https://github.com/sephiroth74/android-target-tooltip/issues/122#issuecomment-468319757
        val tooltip = Tooltip.Builder(context)
            .anchor(this)
            .arrow(true)
            .closePolicy(ClosePolicy.TOUCH_ANYWHERE_NO_CONSUME)
            .text(resources.getString(text))
            .styleId(R.style.ToolTipLayoutDefaultStyle)
            .create()
            .doOnHidden { onDismiss() }
            .apply { show(rootView, gravity) }

        setTag(R.id.tooltip, tooltip)
    }
}