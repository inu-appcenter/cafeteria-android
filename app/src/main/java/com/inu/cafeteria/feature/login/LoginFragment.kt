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
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.finishActivity
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.widget.ReorderableAdapterWrapper
import com.inu.cafeteria.databinding.CafeteriaReorderFragmentBinding
import com.inu.cafeteria.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.loading_view
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.toolbar_reorder
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.koin.core.inject

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val eventHub: EventHub by inject()

    override fun onCreateView(viewCreator: ViewCreator) =
        viewCreator<LoginFragmentBinding> {
            initializeView(root)
            vm = viewModel
        }

    private fun initializeView(view: View) {
        setSupportActionBar(view.toolbar_login, showTitle = true, showUpButton = true)

        with(eventHub) {
            observe(loginEvent) {
                finishActivity()
            }
        }
    }
}
