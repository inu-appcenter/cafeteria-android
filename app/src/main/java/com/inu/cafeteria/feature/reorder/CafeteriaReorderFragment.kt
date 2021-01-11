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

package com.inu.cafeteria.feature.reorder

import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.widget.ReorderableAdapterWrapper
import com.inu.cafeteria.databinding.CafeteriaReorderFragmentBinding

class CafeteriaReorderFragment : BaseFragment<CafeteriaReorderFragmentBinding>() {

    private val viewModel: CafeteriaReorderViewModel by viewModels()

    private val adapterWrapper =
        ReorderableAdapterWrapper(
            adapterFactory = { CafeteriaReorderAdapter(onDragStart = it::startDrag) },
            onItemChange = {
                viewModel.onChangeOrder(it.items.toOrderArray())
            }
        )

    override fun onCreateView(create: ViewCreator) =
        create<CafeteriaReorderFragmentBinding> {
            initializeView(this)
            vm = viewModel
        }

    private fun initializeView(binding: CafeteriaReorderFragmentBinding) {
        setSupportActionBar(binding.toolbarReorder, showTitle = true, showUpButton = true)

        with(binding.cafeteriaSortRecycler) {
            adapterWrapper.setWithRecyclerView(this)

            with(adapterWrapper.adapter) {
                emptyView = binding.emptyViewGroup
                loadingView = binding.loadingView
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetch()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("cafeteriaToReorder")
        fun setCafeteria(view: RecyclerView, cafeteria: List<CafeteriaReorderView>?) {
            cafeteria?.let {
                (view.adapter as? CafeteriaReorderAdapter)?.items = it.toMutableList()
            }
        }
    }
}
