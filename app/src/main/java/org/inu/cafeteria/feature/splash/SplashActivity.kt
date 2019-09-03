package org.inu.cafeteria.feature.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.inu.cafeteria.common.base.SingleFragmentActivity
import org.inu.cafeteria.usecase.GetVersion
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.IOException

class SplashActivity : SingleFragmentActivity() {
    override val fragment: Fragment = SplashFragment()

    companion object {
        fun callingIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }
}