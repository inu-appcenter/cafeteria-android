package org.inu.cafeteria.common.extension

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