package org.inu.cafeteria.common.base

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import org.inu.cafeteria.common.extension.setVisible

abstract class ViewPagerAdapter : BasePagerAdapter() {
    var layouts: List<Int> = ArrayList()
}