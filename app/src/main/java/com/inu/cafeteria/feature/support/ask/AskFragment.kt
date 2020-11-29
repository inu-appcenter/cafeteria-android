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

package com.inu.cafeteria.feature.support.ask

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.databinding.AskFragmentBinding

class AskFragment : BaseFragment() {

    private val viewModel: AskViewModel by viewModels()

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<AskFragmentBinding> {
            init()
            vm = viewModel
        }
    }

    private fun init() {
        with(viewModel) {
            load()

            observe(submitSuccessEvent) {
                notifyUserYouSucceeded()
                navigateBack()
            }
        }
    }

    private fun notifyUserYouSucceeded() {
        Toast.makeText(activity, getString(R.string.notify_ask_succeeded), Toast.LENGTH_SHORT).show()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }
}