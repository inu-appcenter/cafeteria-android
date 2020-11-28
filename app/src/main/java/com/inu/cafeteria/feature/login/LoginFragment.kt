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

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.view.*


class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(viewCreator: ViewCreator) =
        viewCreator<LoginFragmentBinding> {
            initializeView(root)
            vm = viewModel
        }

    private fun initializeView(view: View) {
        setSupportActionBar(view.toolbar_login, showTitle = true, showUpButton = true)

        with(view.id_field) {
            requestFocusWithKeyboard()
        }

        with(view.password_field) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    viewModel.performLogin()
                    true
                } else {
                    false
                }
            }
        }

        with(viewModel) {
            observe(loggedInStatus) {
                if (it == true) {
                    view.password_field.hideKeyboard() // Keyboard does not disappear automatically.
                    finishActivity()
                }
            }
        }
    }
}
