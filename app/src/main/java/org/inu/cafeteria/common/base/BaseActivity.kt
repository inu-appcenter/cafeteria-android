package org.inu.cafeteria.common.base

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.inu.cafeteria.common.extension.resolveThemeColor

/**
 * Base class providing navigation bar coloring.
 * By default, the color is [android.R.attr.windowBackground].
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}