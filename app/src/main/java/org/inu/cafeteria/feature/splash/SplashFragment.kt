package org.inu.cafeteria.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.getViewModel

/**
 * Display splash screen, while checking version.
 * Route activity launches this activity by default.
 */
class SplashFragment : BaseFragment() {

    private lateinit var viewModel: SplashViewModel

    private val checkVersion = {
        with(viewModel) {
            tryCheckVersion(
                onFail = ::handleVersionCheckFailure,
                onPass = { showLogin(this@SplashFragment) },
                onUpdate = ::goUpdate,
                onDismiss = { showLogin(this@SplashFragment) }
            )
        }
    }

    init {
        failables += this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel()
        failables += viewModel.failables
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkVersion()
    }
}
