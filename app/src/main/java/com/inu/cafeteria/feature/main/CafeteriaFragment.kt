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

package com.inu.cafeteria.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.onTabSelect
import com.inu.cafeteria.databinding.CafeteriaFragmentBinding
import kotlinx.android.synthetic.main.cafeteria_fragment.view.*
import kotlinx.android.synthetic.main.date_selection_tab_bar.view.*

class CafeteriaFragment : BaseFragment() {

    private val viewModel: CafeteriaViewModel by viewModels()

    private lateinit var animator: PageSwapAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onSelectDateTab(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = CafeteriaFragmentBinding
        .inflate(inflater, container, false)
        .apply { lifecycleOwner = this@CafeteriaFragment }
        .apply { initializeView(root) }
        .apply { vm = viewModel }
        .root

    private fun initializeView(view: View) {
        with(view.cafeteria_recycler) {
            adapter = CafeteriaAdapter().apply { onClickMore = viewModel::onViewMore }
            animator = PageSwapAnimator(this)
        }

        with(view.date_selector) {
            onTabSelect {
                it?.let {
                    viewModel.onSelectDateTab(it.position)
                    animator.onNewTabSelected(it.position)
                }
            }
        }
    }

    /**
     * Do an animation like that of page swapping.
     * This executes an in-place animation with a single view.
     */
    class PageSwapAnimator(private val animationTarget: View) {

        private var currentSelectedTabPosition = 0

        fun onNewTabSelected(newlySelectedTabPosition: Int) {
            with(animationTarget) {
                alpha = 0f
                x += -30f * getWhichDirectionToSwipe(newlySelectedTabPosition)
                animate().alpha(1f).x(0f)
            }
        }

        private fun getWhichDirectionToSwipe(newlySelectedTabPosition: Int): Int {
            val theAnswer = when {
                newlySelectedTabPosition > currentSelectedTabPosition -> -1
                newlySelectedTabPosition < currentSelectedTabPosition -> +1
                else -> 0
            }

            currentSelectedTabPosition = newlySelectedTabPosition

            // -1: left, 1: right.
            return theAnswer
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("cafeteria")
        fun setCafeteria(view: RecyclerView, cafeteria: List<CafeteriaView>?) {
            cafeteria?.let {
                (view.adapter as? CafeteriaAdapter)?.cafeteria = it
            }
        }
    }
}