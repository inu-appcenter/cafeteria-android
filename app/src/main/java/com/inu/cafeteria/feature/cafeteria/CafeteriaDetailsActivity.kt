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

package com.inu.cafeteria.feature.cafeteria

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.inu.cafeteria.common.base.SingleFragmentActivity
import com.inu.cafeteria.model.json.Cafeteria

class CafeteriaDetailsActivity : SingleFragmentActivity() {
    // Handle swipe gesture.
    private val touchHandler = TouchHandler()

    // Go lazy or it will be null.
    override val fragment: Fragment by lazy {
        CafeteriaDetailFragment.forCafeteria(
            intent.getSerializableExtra(INTENT_EXTRA_PARAM_CAFETERIA) as Cafeteria
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instantiated = true
        touchHandler.setOnSwipeDown {
            (fragment as CafeteriaDetailFragment).onSwipeDown()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instantiated = false
    }

    /**
     * Intercept all touch event in this activity.
     */

override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        this.onTouchEvent(ev)

        return super.dispatchTouchEvent(ev)
    }

    /**
     * Process touch event before giving it back to system.
     */

override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchHandler.onTouchEvent(event)

        return super.onTouchEvent(event)
    }


    inner class TouchHandler {
        private var y1: Float = 0f
        private var y2: Float = 0f

        private var onSwipeDown: () -> Unit = {}

        fun onTouchEvent(event: MotionEvent?) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    y1 = event.y
                }

                MotionEvent.ACTION_UP -> {
                    y2 = event.y

                    if (y2 - y1 > 250) {
                        this.onSwipeDown()
                    }
                }
            }
        }

        fun setOnSwipeDown(onSwipeDown: () -> Unit) {
            this.onSwipeDown = onSwipeDown
        }
    }

    companion object {
        fun callingIntent(context: Context, cafeteria: Cafeteria): Intent {
            return Intent(context, CafeteriaDetailsActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_CAFETERIA, cafeteria)
            }
        }

        private const val INTENT_EXTRA_PARAM_CAFETERIA = "com.inu.INTENT_PARAM_CAFETERIA"

        var instantiated: Boolean = false
            private set
    }
}

