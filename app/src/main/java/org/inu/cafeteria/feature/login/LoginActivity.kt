package org.inu.cafeteria.feature.login

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import org.inu.cafeteria.common.base.SingleFragmentActivity

class LoginActivity : SingleFragmentActivity() {
    override val fragment: Fragment = LoginFragment()

    companion object {
        fun callingIntent(context: Context) = Intent(context, SingleFragmentActivity::class.java)
    }
}