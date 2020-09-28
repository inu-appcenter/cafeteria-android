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
import android.view.*
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.databinding.CafeteriaFragmentBinding
import com.inu.cafeteria.extension.afterDays
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.extension.withNonNull
import kotlinx.android.synthetic.main.cafeteria_fragment.view.*
import kotlinx.android.synthetic.main.date_selection_tab_bar.view.*
import kotlinx.android.synthetic.main.empty_view.view.*
import java.util.*

class CafeteriaFragment : BaseFragment() {

    override val optionMenuId: Int? = R.menu.cafeteria_menu

    private val viewModel: CafeteriaViewModel by navGraphViewModels(R.id.nav_graph_cafeteria)
    private val pagingManager = PagingManager()
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
            adapter = CafeteriaAdapter().apply {
                onClickMore = viewModel::onViewMore
                emptyView = view.empty_view
                loadingView = view.loading_view
            }

            pagingManager.animationTarget = this
        }

        with(view.date_selector) {
            onTabSelect {
                it?.let {
                    viewModel.onSelectDateTab(it.position)
                    pagingManager.onNewTabSelected(it.position)
                }
            }
        }

        with(view.logo_image) {
            withinAlphaAnimation(0f, 1f)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.apply {
            preFetch(5)

            observe(moreClickEvent) {
                it?.let(::showCafeteriaDetails)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        view?.cafeteria_recycler?.withinAlphaAnimation(0f, 1f) {
            viewModel.onSelectDateTab(pagingManager.getCurrentlySelectedTabPosition())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        viewModel.onClickOptionMenu(item.itemId)

    private fun showCafeteriaDetails(cafeteria: CafeteriaView) {
        val bundle = bundleOf(
            "cafeteriaId" to cafeteria.id,
            "date" to Date().afterDays(pagingManager.getCurrentlySelectedTabPosition()).format()
        )

        findNavController().navigate(R.id.action_cafeteria_detail, bundle)
    }

    /**
     * Do an animation like that of page swapping.
     * This executes an in-place animation with a single view.
     */
    class PagingManager {

        private var currentSelectedTabPosition = 0

        var animationTarget: View? = null

        fun getCurrentlySelectedTabPosition() = currentSelectedTabPosition

        fun onNewTabSelected(newlySelectedTabPosition: Int) {
            withNonNull(animationTarget) {
                alpha = 0f
                x += -30f * getWhichDirectionToSwipe(newlySelectedTabPosition)
                animate().alpha(1f).x(0f)
            }

            currentSelectedTabPosition = newlySelectedTabPosition
        }

        private fun getWhichDirectionToSwipe(newlySelectedTabPosition: Int): Int {
            val theAnswer = when {
                newlySelectedTabPosition > currentSelectedTabPosition -> -1
                newlySelectedTabPosition < currentSelectedTabPosition -> +1
                else -> 0
            }

            // -1: left, 1: right.
            return theAnswer
        }
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
            (view.adapter as? BaseAdapter<*>)?.isLoading = isLoading ?: true
        }
    }
}