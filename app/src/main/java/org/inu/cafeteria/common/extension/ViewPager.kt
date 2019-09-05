package org.inu.cafeteria.common.extension

import androidx.viewpager.widget.ViewPager

fun ViewPager.addOnPageChangeListener(listener: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            listener(position)
        }
    })
}
