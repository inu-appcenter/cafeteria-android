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

package com.inu.cafeteria.feature.support.notice

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.databinding.NoticeFragmentBinding
import kotlinx.android.synthetic.main.notice_fragment.view.*

class NoticeFragment : BaseFragment() {

    private val viewModel: NoticeViewModel by viewModels()

    private val adapter = NoticeAdapter()

    override fun onNetworkChange(available: Boolean) {
        if (available) {
            viewModel.load()
        }
    }

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<NoticeFragmentBinding> {
            initializeView(root)
            vm = viewModel
        }
    }

    private fun initializeView(view: View) {
        with(view.notice_recycler) {
            adapter = this@NoticeFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(adapter) {
            emptyView = view.empty_view
            loadingView = view.loading_view
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("notices")
        fun setNotices(view: RecyclerView, notices: List<NoticeView>?) {
            notices?.let {
                (view.adapter as? NoticeAdapter)?.data = it
            }
        }

        @JvmStatic
        @BindingAdapter("isNoticesLoading")
        fun setNoticesLoading(view: RecyclerView, isNoticesLoading: Boolean?) {
            (view.adapter as? NoticeAdapter)?.isLoading = isNoticesLoading ?: true
        }
    }
}