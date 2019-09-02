package org.inu.cafeteria.feature.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.inu.cafeteria.common.base.SingleFragmentActivity
import org.inu.cafeteria.usecase.GetVersion
import org.koin.android.ext.android.inject
import timber.log.Timber

class SplashActivity : SingleFragmentActivity() {
    override val fragment: Fragment = SplashFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Test succeeded 20190903
        val getVersion: GetVersion by inject()

        getVersion(Unit) {
            it.onSuccess {
                Timber.i("Current version is ${it.currentVersion}, latest is ${it.latestVersion}, so update needed? ${it.needUpdate()}.")
            }.onError {
                Timber.e("No!!")
            }
        }
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }
}