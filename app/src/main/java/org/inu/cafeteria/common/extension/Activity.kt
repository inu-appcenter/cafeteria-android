package org.inu.cafeteria.common.extension

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import org.inu.cafeteria.extension.withNonNull

fun AppCompatActivity.setSupportActionBar(toolbar: Toolbar, title: Boolean = false, upButton: Boolean = false) {
    setSupportActionBar(toolbar)

    withNonNull(supportActionBar) {
        setDisplayShowTitleEnabled(title)
        setDisplayHomeAsUpEnabled(upButton)
    }
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}