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
import android.view.MenuItem
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.databinding.CafeteriaFragmentBinding
import kotlinx.android.synthetic.main.cafeteria_fragment.view.*
import kotlinx.android.synthetic.main.date_selection_tab_bar.view.*
import kotlinx.android.synthetic.main.empty_view.view.*
import org.koin.core.inject

class CafeteriaFragment : BaseFragment() {

    override val optionMenuId: Int? = R.menu.cafeteria_menu

    private val viewModel: CafeteriaViewModel by navGraphViewModels(R.id.nav_graph_cafeteria)
    private val eventHub: EventHub by inject()

    private val adapter = CafeteriaAdapter()

    private var persistentView: View? = null

    override fun onCreateView(viewCreator: ViewCreator) =
        persistentView?.apply { removeFromParent() } ?:
        viewCreator<CafeteriaFragmentBinding> {
            initializeView(root)
            vm = viewModel
            persistentView = root
        }

    private fun initializeView(view: View) {
        setSupportActionBar(view.toolbar_cafeteria)

        with(view.cafeteria_recycler) {

            adapter = this@CafeteriaFragment.adapter.apply {
                onClickMore = viewModel::onViewMore
                emptyView = view.empty_view
                loadingView = view.loading_view

                positions = viewModel.menuPagePositions
            }
        }

        with(view.date_selector) {
            // Restoring tab position.
            getTabAt(viewModel.currentDateTabPosition)?.select()

            onTabSelect {
                viewModel.onSelectDateTab(it.position)
            }
        }

        with(view.logo_image) {
            withinAlphaAnimation(0f, 1f)
        }

        with(viewModel) {
            observe(moreClickEvent) {
                showCafeteriaDetails()
            }

            observe(animateEvent) {
                view.cafeteria_recycler.slideInWithFade(it ?: 0)
            }
        }

        with(eventHub) {
            observe(reorderEvent) {
                reloadCurrentTab()
            }
        }
    }

    private fun reloadCurrentTab() {
        view?.cafeteria_recycler?.withinAlphaAnimation(0f, 1f) {
            viewModel.reselectCurrentDateTab()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        viewModel.onClickOptionMenu(item.itemId)

    private fun showCafeteriaDetails() {
        findNavController().navigate(R.id.action_cafeteria_detail)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.onSaveMenuPagePositions(adapter.positions)

        super.onSaveInstanceState(outState)
    }

    companion object {

        @JvmStatic
        @BindingAdapter("cafeteria")
        fun setCafeteria(view: RecyclerView, cafeteria: List<CafeteriaView>?) {
            cafeteria?.let {
                (view.adapter as? CafeteriaAdapter)?.data = it
            }
        }

        @JvmStatic
        @BindingAdapter("isLoading")
        fun setLoading(view: RecyclerView, isLoading: Boolean?) {
            (view.adapter as? CafeteriaAdapter)?.isLoading = isLoading ?: true
        }
    }
}