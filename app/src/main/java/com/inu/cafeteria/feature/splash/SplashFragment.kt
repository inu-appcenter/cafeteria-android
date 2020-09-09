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
 * Display splash screen, while checking version and notices.
 * Route activity launches this activity by default.
 */

class SplashFragment : BaseFragment() {

    private val navigator: Navigator by inject()

    private lateinit var viewModel: SplashViewModel

    private val checkVersion = { nextStep: () -> Unit ->
        with(viewModel) {
            tryCheckVersion(
                activity = activity!!,
                onFail = ::handleConnectionFailure,
                onPass = nextStep,
                onUpdate = {
                    // Before moving to store, take a next step.
                    nextStep()
                    goUpdate()
                },
                onDismiss = nextStep
            )
        }
    }

    private val checkNotices = { nextStep: () -> Unit ->
        with(viewModel) {
            tryShowNotice(
                activity = activity!!,
                onFail = ::handleConnectionFailure,
                onPass = nextStep,
                onConfirm = nextStep
            )
        }
    }

    private val routine = {
        checkVersion {
            checkNotices {
                viewModel.showLogin(this)
            }
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
            onNoConnection = { navigator.showNoConnectionDialog(activity!!, routine) }
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

        routine()
    }
}
