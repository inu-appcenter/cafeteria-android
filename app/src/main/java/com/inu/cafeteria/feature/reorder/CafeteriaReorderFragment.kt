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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.widget.ReorderableAdapterWrapper
import com.inu.cafeteria.databinding.CafeteriaReorderFragmentBinding
import kotlinx.android.synthetic.main.cafeteria_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.loading_view
import kotlinx.android.synthetic.main.empty_view.view.*

class CafeteriaReorderFragment : BaseFragment() {

    private val viewModel: CafeteriaReorderViewModel by viewModels()

    private val adapterWrapper =
        ReorderableAdapterWrapper(CafeteriaReorderAdapter()) { adapter ->
            viewModel.onChangeOrder(adapter.data.map { it.id }.toTypedArray())
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = CafeteriaReorderFragmentBinding
        .inflate(inflater, container, false)
        .apply { lifecycleOwner = this@CafeteriaReorderFragment }
        .apply { initializeView(root) }
        .apply { vm = viewModel }
        .root

    private fun initializeView(view: View) {
        with(view.cafeteria_sort_recycler) {
            adapterWrapper.setWithRecyclerView(this)
            with(adapterWrapper.adapter) {
                emptyView = view.empty_view
                loadingView = view.loading_view
            }
        }

        setSupportActionBar(view.toolbar_sort, showTitle = true, showUpButton = true)
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