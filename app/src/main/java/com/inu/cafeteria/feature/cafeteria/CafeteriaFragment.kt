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
import com.inu.cafeteria.extension.withNonNull
import it.sephiroth.android.library.xtooltip.Tooltip
import org.koin.core.inject

class CafeteriaFragment : BaseFragment() {

    override val toolbarId = R.id.toolbar_cafeteria
    override val optionMenuId = R.menu.cafeteria_menu
    override val setupToolbarForNavigation = true

    private lateinit var binding: CafeteriaFragmentBinding
    private val viewModel: CafeteriaViewModel by navGraphViewModels(R.id.nav_graph_cafeteria)
    private val eventHub: EventHub by inject()

    private val adapter = CafeteriaAdapter()

    // Reuse view after lifecycle!
    private var persistentView: View? = null

    override fun onNetworkStateChange(available: Boolean) {
        if (available) {
            viewModel.load()
        }
    }

    override fun onCreateView(viewCreator: ViewCreator) =
        persistentView?.apply { removeFromParent() } ?:
        viewCreator<CafeteriaFragmentBinding> {
            initializeView(this)
            vm = viewModel
            persistentView = root
            binding = this
        }

    private fun initializeView(binding: CafeteriaFragmentBinding) {
        with(binding.cafeteriaRecycler) {
            adapter = this@CafeteriaFragment.adapter.apply {
                onClickMore = viewModel::onViewMore
                emptyView = binding.emptyView.emptyView
                loadingView = binding.loadingView

                positions = viewModel.menuPagePositions
            }
        }

        with(binding.dateSelectionPart.dateSelector) {
            // Restoring tab position.
            getTabAt(viewModel.currentDateTabPosition)?.select()

            onTabSelect {
                viewModel.onSelectDateTab(it.position)
            }
        }

        with(binding.logoImage) {
            withinAlphaAnimation(0f, 1f)
        }

        with(viewModel) {
            observe(sortingHintEvent) {
                it ?: return@observe

                withNonNull(activity?.findViewById<View>(R.id.menu_reorder)) {
                    showTooltip(context, binding.root, Tooltip.Gravity.LEFT, it.hintText) {
                        viewModel.markHintShown()
                    }
                }
            }

            observe(moreClickEvent) {
                showCafeteriaDetails()
            }

            observe(animateEvent) {
                with(binding.cafeteriaRecycler) {
                    slideInWithFade(it ?: 0)
                }
            }
        }

        with(eventHub) {
            observe(reorderEvent) {
                reloadCurrentTab()
            }
        }
    }

    private fun reloadCurrentTab() {
        withNonNull(view?.findViewById<RecyclerView>(R.id.cafeteria_recycler)) {
            withinAlphaAnimation(0f, 1f) {
                viewModel.reselectCurrentDateTab()
            }
        }
    }

    private fun showCafeteriaDetails() {
        findNavController().navigate(R.id.action_cafeteria_detail)
    }

    override fun onResume() {
        super.onResume()

        viewModel.emitHintEvent()
    }

    override fun onPause() {
        super.onPause()

        clearTooltip()
    }

    private fun clearTooltip() {
        binding.root.dismissTooltip()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        viewModel.onClickOptionMenu(item.itemId)

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