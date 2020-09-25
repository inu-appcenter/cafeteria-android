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
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.databinding.CafeteriaDetailFragmentBinding
import com.inu.cafeteria.feature.main.MenuAdapter
import com.inu.cafeteria.feature.main.MenuPageAdapter
import com.inu.cafeteria.feature.main.MenuView
import kotlinx.android.synthetic.main.cafeteria_detail_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.*
import kotlinx.android.synthetic.main.empty_cafeteria_view.view.*
import kotlinx.android.synthetic.main.empty_view.view.*

class CafeteriaDetailFragment : BaseFragment() {

    private val viewModel: CafeteriaDetailViewModel by getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleArguments()
    }

    private fun handleArguments() {
        val cafeteriaId = arguments?.getInt("cafeteriaId", -1)?.takeIf { it != -1 }
        val date = arguments?.getString("date")

        viewModel.load(cafeteriaId ?: return, date ?: return) {
            supportActionBar?.title = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = CafeteriaDetailFragmentBinding
        .inflate(inflater, container, false)
        .apply { lifecycleOwner = this@CafeteriaDetailFragment }
        .apply { initializeView(root) }
        .apply { vm = viewModel }
        .root

    private fun initializeView(view: View) {
        setSupportActionBar(view.toolbar_cafeteria_detail, showTitle = true, showUpButton = true)

        with(view.menu_page_recycler) {
            adapter = MenuPageAdapter(pageSize = 0 /* no paging */).apply {
                emptyView = view.empty_cafeteria_view
            }
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("menus")
        fun setCafeteria(view: RecyclerView, menus: List<MenuView>?) {
            menus?.let {
                (view.adapter as? MenuPageAdapter)?.data = it
            }
        }
    }
}