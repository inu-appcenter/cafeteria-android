package org.inu.cafeteria.common.extension

import android.animation.LayoutTransition
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

var ViewGroup.animateLayoutChanges: Boolean
    get() = layoutTransition != null
    set(value) {
        layoutTransition = if (value) LayoutTransition() else null
    }


fun ImageView.setTint(color: Int) {
    imageTintList = ColorStateList.valueOf(color)
}

fun ImageView.setTintRes(@ColorRes colorResId: Int) {
    setTint(resources.getColor(colorResId, null))
}

fun ProgressBar.setTint(color: Int) {
    indeterminateTintList = ColorStateList.valueOf(color)
    progressTintList = ColorStateList.valueOf(color)
}

fun View.setBackgroundTint(color: Int) {

    // API 21 doesn't support this
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
        background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    backgroundTintList = ColorStateList.valueOf(color)
}

fun View.setPadding(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    setPadding(left ?: paddingLeft, top ?: paddingTop, right ?: paddingRight, bottom ?: paddingBottom)
}

var View.isVisible: Boolean
    get() = this.visibility == View.VISIBLE
    set(visible) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

fun View.setVisible(visible: Boolean, invisible: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else invisible
}

/**
 * If a view captures clicks at all, then the parent won't ever receive touch events. This is a
 * problem when we're trying to capture link clicks, but tapping or long pressing other areas of
 * the view no longer work. Also problematic when we try to long press on an image in the message
 * view
 */
fun View.forwardTouches(parent: View) {
    var isLongClick = false

    setOnLongClickListener {
        isLongClick = true
        true
    }

    setOnTouchListener { v, event ->
        parent.onTouchEvent(event)

        when {
            event.action == MotionEvent.ACTION_UP && isLongClick -> {
                isLongClick = true
                true
            }

            event.action == MotionEvent.ACTION_DOWN -> {
                isLongClick = false
                v.onTouchEvent(event)
            }

            else -> v.onTouchEvent(event)
        }
    }
}

fun ViewPager.addOnPageChangeListener(listener: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            listener(position)
        }
    })
}

fun RecyclerView.scrapViews() {
    val adapter = adapter
    val layoutManager = layoutManager

    this.adapter = null
    this.layoutManager = null

    this.adapter = adapter
    this.layoutManager = layoutManager

    adapter?.notifyDataSetChanged()
}

fun TextView.setBold(bold: Boolean) {
    // TODO default value
    this.setTypeface(this.typeface, if (bold) Typeface.BOLD else Typeface.NORMAL)
}

fun TextView.setTextColorRes(@ColorRes colorResId: Int) {
    resources.getColor(colorResId, null)
}
