/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.common.extension

import android.content.Context
import android.view.Menu
import android.view.MenuItem

/** Returns a [MutableIterator] over the items in this menu. */
operator fun Menu.iterator() = object : MutableIterator<MenuItem> {
    private var index = 0
    override fun hasNext() = index < size()
    override fun next() = getItem(index++) ?: throw IndexOutOfBoundsException()
    override fun remove() = removeItem(--index)
}

fun Menu.setTint(context: Context?, color: Int) {
    context?.let {
        iterator().forEach { item ->
            item.icon?.setTint(color)
        }
    }
}

fun Menu.setVisible(visible: Boolean) {
    iterator().forEach {
        it.isVisible = visible
    }
}