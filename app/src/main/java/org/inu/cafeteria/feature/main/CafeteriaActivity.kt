package org.inu.cafeteria.feature.main

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.SingleFragmentActivity

class CafeteriaActivity : SingleFragmentActivity() {
    override val fragment: Fragment = CafeteriaFragment()
    override val layoutId: Int = R.layout.cafeteria_activity
    override val toolbarId: Int? = R.id.toolbar

    companion object {
        fun callingIntent(context: Context) = Intent(context, CafeteriaActivity::class.java)
    }
}