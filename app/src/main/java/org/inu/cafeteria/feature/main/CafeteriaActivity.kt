package org.inu.cafeteria.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.toolbar.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.SingleFragmentActivity
import org.inu.cafeteria.common.extension.setSupportActionBar

class CafeteriaActivity : SingleFragmentActivity() {
    override val fragment: Fragment = CafeteriaFragment()
    override val layoutId: Int = R.layout.cafeteria_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar, title = false, upButton = true)
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, CafeteriaActivity::class.java)
    }
}