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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.widget.ItemTouchHelperAdapter
import com.inu.cafeteria.databinding.CafeteriaReorderFragmentBinding
import kotlinx.android.synthetic.main.cafeteria_reorder_fragment.view.*

class CafeteriaReorderFragment : BaseFragment() {

    private val viewModel: CafeteriaReorderViewModel by viewModels()
    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            (adapter as ItemTouchHelperAdapter).onItemMove(viewHolder.adapterPosition, target.adapterPosition);
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (adapter as ItemTouchHelperAdapter).onItemDismiss(viewHolder.adapterPosition)
        }
    })

    private val adapter: CafeteriaReorderAdapter = CafeteriaReorderAdapter(itemTouchHelper::startDrag)

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
            adapter = this@CafeteriaReorderFragment.adapter
            itemTouchHelper.attachToRecyclerView(this)
        }

        setSupportActionBar(view.toolbar_sort, showTitle = true, showUpButton = true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.fetch()
    }

    companion object {

        @JvmStatic
        @BindingAdapter("cafeteriaToReorder")
        fun setCafeteria(view: RecyclerView, cafeteria: List<CafeteriaReorderView>?) {
            cafeteria?.let {
                (view.adapter as? CafeteriaReorderAdapter)?.cafeteria = it.toMutableList()
            }
        }
    }
}