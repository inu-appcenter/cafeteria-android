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
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import com.inu.cafeteria.extension.withNonNull
import com.inu.cafeteria.util.Notify

/**
 * Do something in the middle of beginTransaction() and commitNow().
 */
inline fun FragmentManager.inImmediateTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitNow()
}


inline fun <reified T : ViewModel> Fragment.getViewModel(): Lazy<T> {
    return ViewModelLazy(T::class, ::getViewModelStore, ::getDefaultViewModelProviderFactory)
}

val Fragment.baseActivity: AppCompatActivity? get() = (activity as? AppCompatActivity)
val Fragment.appContext: Context get() = activity?.applicationContext!!

val Fragment.supportActionBar: ActionBar? get() = (activity as? AppCompatActivity)?.supportActionBar

fun Fragment.setSupportActionBar(toolbar: Toolbar?, showTitle: Boolean = false, showUpButton: Boolean = false) {
    toolbar ?: return

    withNonNull(activity as? AppCompatActivity) {
        setSupportActionBar(toolbar)

        withNonNull(supportActionBar) {
            setDisplayShowTitleEnabled(showTitle)
            setDisplayHomeAsUpEnabled(showUpButton)
        }
    }
}

fun Fragment.setToolbar(
    @IdRes toolbarId: Int,
    @MenuRes menuId: Int,
    onClick: (MenuItem) -> Boolean = this::onOptionsItemSelected
) {
    activity?.findViewById<Toolbar>(toolbarId)?.let {
        setToolbar(it, menuId, onClick)
    }
}

fun Fragment.setToolbar(
    toolbar: Toolbar,
    @MenuRes menuId: Int,
    onClick: (MenuItem) -> Boolean = this::onOptionsItemSelected
) {
    with(toolbar) {
        menu.clear()
        inflateMenu(menuId)
        setOnMenuItemClickListener(onClick)
    }
}

fun Fragment.notify(message: String?, long: Boolean = false) {
    message?.let {
        with(Notify(context)) {
            if (long) long(it) else short(it)
        }
    }
}

fun Fragment.finishActivity() {
    activity?.finish()
}

fun Fragment.hideKeyboard() {
    baseActivity?.hideKeyboard()
}