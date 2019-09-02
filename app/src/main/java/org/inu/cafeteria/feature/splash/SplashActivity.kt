package org.inu.cafeteria.feature.splash

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import org.inu.cafeteria.common.base.SingleFragmentActivity

class SplashActivity : SingleFragmentActivity() {
    override val fragment: Fragment = SplashFragment()

    companion object {
        fun callingIntent(context: Context) = Intent(context, SingleFragmentActivity::class.java)
    }
}