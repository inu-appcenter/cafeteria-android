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

package com.inu.cafeteria.feature.cafeteria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.getViewModel
import com.inu.cafeteria.databinding.CafeteriaListFragmentBinding
import kotlinx.android.synthetic.main.cafeteria_list_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_list_item.view.*
import org.koin.core.inject
import timber.log.Timber

class CafeteriaListFragment : BaseFragment() {

    private val navigator: Navigator by inject()

    private val cafeteriaAdapter = CafeteriaAdapter()

    private lateinit var cafeteriaListViewModel: CafeteriaListViewModel
    private lateinit var viewDataBinding: CafeteriaListFragmentBinding

    init {
        failables += this
        failables += cafeteriaAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cafeteriaListViewModel = getViewModel()
        failables += cafeteriaListViewModel.failables
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return CafeteriaListFragmentBinding
            .inflate(inflater)
            .apply { lifecycleOwner = this@CafeteriaListFragment }
            .apply { viewDataBinding = this }
            .apply { vm = cafeteriaListViewModel }
            .apply { initializeView(root) }
            .apply { cafeteriaListViewModel.loadAll() }
            .root
    }

    private fun initializeView(view: View) {
        with(view.cafeteria_list) {
            cafeteriaAdapter.clickListener = { view, item ->
                navigator.showCafeteriaDetail(
                    activity!!,
                    item,
                    view.cafeteria_image,
                    view.cafeteria_title
                )
            }
            adapter = cafeteriaAdapter
        }

        Timber.i("CafeteriaListFragment initialized.")
    }
}