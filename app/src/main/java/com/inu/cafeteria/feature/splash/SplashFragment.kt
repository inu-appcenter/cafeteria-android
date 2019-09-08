/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.getViewModel
import org.koin.core.inject

/**
 * Display splash screen, while checking version.
 * Route activity launches this activity by default.
 *
 * TODO: Explanation && Permission
 * We need to put some explanation on this app for user.
 * Also we need to ask for accessing settings.
 * This is required because a barcode feature
 * needs a bright screen.
 */
class SplashFragment : BaseFragment() {

    private val navigator: Navigator by inject()

    private lateinit var viewModel: SplashViewModel

    private val checkVersion = {
        with(viewModel) {
            tryCheckVersion(
                activity = activity!!,
                onFail = ::handleVersionCheckFailure,
                onPass = { showLogin(this@SplashFragment) },
                onUpdate = {
                    // Before moving to store, take next step to login.
                    showLogin(this@SplashFragment)
                    goUpdate()
                },
                onDismiss = { showLogin(this@SplashFragment) }
            )
        }
    }

    init {
        failables += this
        failables += navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel {
            // This action cannot be done inside view model because it needs activity.
            onServerNoResponse = { navigator.showServerDeadDialog(activity!!) }
            onUnknownError = { navigator.showFatalDialog(activity!!, it) }
        }
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
