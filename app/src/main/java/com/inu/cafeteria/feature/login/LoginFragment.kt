/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.login

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.getViewModel
import com.inu.cafeteria.common.extension.hideKeyboard
import com.inu.cafeteria.common.extension.isVisible
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.extension.withNonNull
import com.inu.cafeteria.repository.LoginRepository
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.koin.core.inject
import timber.log.Timber

class LoginFragment : BaseFragment() {

    private val loginRepo: LoginRepository by inject()

    private lateinit var viewModel: LoginViewModel

    private val onLoginButtonClick = { root: View ->
        val id = root.student_id.text.toString()
        val pw = root.password.text.toString()
        val auto = root.autologin.isChecked

        with(viewModel) {
            tryLoginWithIdAndPw(
                id = id,
                pw = pw,
                auto = auto,
                onSuccess = {
                    Timber.i("Login succeeded with id and password.")

                    saveLoginResult(it, id)
                    showMain(this@LoginFragment)
                },
                onFail = {
                    Timber.i("Login failed.")

                    handleLoginFailure(it)
                }
            )
        }
    }

    init {
        failables += this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel {
            // If already logged in, pass this screen.
            if (passIfLoggedIn(this@LoginFragment)) {
                return
            }

            tryAutoLogin(
                onNoToken = {
                    Timber.i("No token available. Try login with id and password.")
                },
                onSuccess = {
                    Timber.i("Auto login succeeded.")
                    showMain(this@LoginFragment)
                },
                onFail = ::handleAutoLoginFailure
             )
        }
        failables += viewModel.failables
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false).apply {
            initializeView(this)
        }
    }

    private fun initializeView(view: View) {
        with(view.loading_layout) {
            observe(viewModel.loginInProgress) {
                it?.let {
                    // Show loading UI while blocking touches to other objects.
                    isVisible = it
                }
            }
        }

        with(view.login) {
            setOnClickListener { onLoginButtonClick(view) }
        }

        with(view.no_user_login) {
            setOnClickListener { viewModel.showMain(this@LoginFragment) }
        }

        with(view.root_layout) {
            setOnClickListener { hideKeyboard() }

            withNonNull(background as? AnimationDrawable){
                setExitFadeDuration(2000)
                start()
            }
        }
    }
}