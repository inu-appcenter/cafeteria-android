package com.inu.cafeteria.feature.login

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inu.cafeteria.common.base.SingleFragmentActivity

class LoginActivity : SingleFragmentActivity() {
    override val fragment: Fragment = LoginFragment()

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}