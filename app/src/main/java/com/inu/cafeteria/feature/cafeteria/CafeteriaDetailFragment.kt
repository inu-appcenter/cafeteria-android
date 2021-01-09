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

import androidx.databinding.BindingAdapter
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.setLeftInsetDivider
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.extension.supportActionBar
import com.inu.cafeteria.databinding.CafeteriaDetailFragmentBinding
import com.inu.cafeteria.extension.withNonNull

class CafeteriaDetailFragment : BaseFragment() {

    private val viewModel: CafeteriaViewModel by navGraphViewModels(R.id.nav_graph_cafeteria)

    override fun onCreateView(create: ViewCreator) =
        create<CafeteriaDetailFragmentBinding> {
            initializeView(this)
            vm = viewModel
        }

    private fun initializeView(binding: CafeteriaDetailFragmentBinding) {
        setSupportActionBar(binding.toolbarCafeteriaDetail, showTitle = true, showUpButton = true)

        withNonNull(supportActionBar) {
            title = viewModel.selected.value?.name
        }

        with(binding.menuRecycler) {
            adapter = MenuAdapter().apply {
                emptyView = binding.emptyViewPart.emptyCafeteriaView

                setLeftInsetDivider(R.drawable.line_divider, R.dimen.menu_left_margin_until_text)
            }
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("menus")
        fun setCafeteria(view: RecyclerView, menus: List<MenuView>?) {
            menus?.let {
                (view.adapter as? MenuAdapter)?.data = it
            }
        }
    }
}