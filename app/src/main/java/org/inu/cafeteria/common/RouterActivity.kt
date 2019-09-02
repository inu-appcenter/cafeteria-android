package org.inu.cafeteria.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Start point of this application.
 *
 * This RouterActivity is not feature dependent,
 * which makes it proper to be a launcher activity.
 * If an initial intent requires something,
 * you can handle it here (further future).
 */
class RouterActivity : AppCompatActivity(), KoinComponent {
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Do other things before the normal routine.

        navigator.showSplash()

        finish()
    }
}