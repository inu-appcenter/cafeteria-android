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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.PositionRetainingAdapter
import com.inu.cafeteria.common.base.PositionRetainingViewHolder
import com.inu.cafeteria.common.extension.onScrollStateChange
import com.inu.cafeteria.common.widget.GenericDiffCallback
import com.inu.cafeteria.databinding.CafeteriaBinding
import timber.log.Timber

class CafeteriaAdapter : PositionRetainingAdapter<CafeteriaView, CafeteriaBinding>() {

    private val menuPagePool = RecyclerView.RecycledViewPool()
    private val menuPool = RecyclerView.RecycledViewPool()

    var onClickMore: (CafeteriaView) -> Any? = {}

    override fun onItemsChanged(old: List<CafeteriaView>, new: List<CafeteriaView>) {
        val diffCallback = GenericDiffCallback(old, new) { id }
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionRetainingViewHolder<CafeteriaBinding> {
        val inflater = LayoutInflater.from(parent.context)

        return CafeteriaViewHolder(CafeteriaBinding.inflate(inflater, parent, false/*Important!!*/))
    }

    override fun onBindViewHolder(holder: PositionRetainingViewHolder<CafeteriaBinding>, position: Int) {
        super.onBindViewHolder(holder, position)

        with(holder as CafeteriaViewHolder) {
            bind(getItem(position))
        }
    }

    inner class CafeteriaViewHolder(binding: CafeteriaBinding) : PositionRetainingViewHolder<CafeteriaBinding>(binding) {

        override val layoutManager: LinearLayoutManager = binding.menuPageRecycler.layoutManager as LinearLayoutManager

        private val menuPageAdapter = MenuPageAdapter(menuPool)

        init {
            Timber.d("Inflate Cafeteria view holder!")

            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(binding.menuPageRecycler) {
                onScrollStateChange { saveViewHolderPosition(this@CafeteriaViewHolder) }

                adapter = menuPageAdapter.apply {
                    emptyView = binding.emptyView
                }

                setRecycledViewPool(menuPagePool)

                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        fun bind(cafeteria: CafeteriaView?) {
            cafeteria ?: return

            with(binding.cafeteriaName) {
                text = cafeteria.name

                setOnClickListener {
                    onClickMore(cafeteria)
                }
            }

            with(binding.moreButton) {
                setOnClickListener {
                    onClickMore(cafeteria)
                }
            }

            menuPageAdapter.items = cafeteria.wholeMenus
        }
    }
}