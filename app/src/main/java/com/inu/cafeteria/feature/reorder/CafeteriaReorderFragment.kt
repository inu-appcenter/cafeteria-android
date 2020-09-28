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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.widget.ReorderableAdapterWrapper
import com.inu.cafeteria.databinding.CafeteriaReorderFragmentBinding
import com.inu.cafeteria.feature.main.CafeteriaViewModel
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.loading_view
import org.koin.core.inject

class CafeteriaReorderFragment : BaseFragment() {

    private val viewModel: CafeteriaReorderViewModel by viewModels()
    private val eventHub: EventHub by inject()

    private val adapterWrapper =
        ReorderableAdapterWrapper(
            adapterFactory = { CafeteriaReorderAdapter(onDragStart = it::startDrag) },
            onItemChange = {
                viewModel.onChangeOrder(it.data.toOrderArray())
                eventHub.reorderEvent.call()
            }
        )

    override fun onCreateView(viewCreator: ViewCreator) =
        viewCreator<CafeteriaReorderFragmentBinding> {
            initializeView(root)
            vm = viewModel
        }

    private fun initializeView(view: View) {
        setSupportActionBar(view.toolbar_reorder, showTitle = true, showUpButton = true)

        with(view.cafeteria_sort_recycler) {
            adapterWrapper.setWithRecyclerView(this)

            with(adapterWrapper.adapter) {
                emptyView = view.empty_view_group
                loadingView = view.loading_view
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
                (view.adapter as? CafeteriaReorderAdapter)?.data = it.toMutableList()
            }
        }
    }
}
