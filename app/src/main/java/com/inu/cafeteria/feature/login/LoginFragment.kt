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