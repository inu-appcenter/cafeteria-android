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

package com.inu.cafeteria.feature.support

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.databinding.SupportFragmentBinding
import kotlinx.android.synthetic.main.kakao_chat_button.view.*
import kotlinx.android.synthetic.main.support_fragment.view.*

class SupportFragment : BaseFragment() {

    private val viewModel: SupportViewModel by viewModels()

    private val adapter = SupportOptionsAdapter()

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<SupportFragmentBinding> {
            initializeView(root)
            vm = viewModel
        }
    }

    private fun initializeView(view: View) {
        with(view.support_options_recycler) {
            adapter = this@SupportFragment.adapter
        }

        with(adapter) {
            onClickRootLayout = {
                findNavController().navigate(it.navigateTo)
            }
        }

        with(view.kakaotalk_button) {
            setOnClickListener { onKakaoTalkClick() }
        }
    }

    private fun onKakaoTalkClick() {
        startActivity(viewModel.getKakaoIntent())
    }

    companion object {

        @JvmStatic
        @BindingAdapter("supportOptions")
        fun setSupportOptions(view: RecyclerView, options: List<SupportOption>?) {
            options?.let {
                (view.adapter as? SupportOptionsAdapter)?.items = it
            }
        }
    }
}