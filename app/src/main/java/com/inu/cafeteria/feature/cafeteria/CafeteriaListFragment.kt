/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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
            .apply { cafeteriaListViewModel.loadAll(firstTimeCreated(savedInstanceState)) }
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